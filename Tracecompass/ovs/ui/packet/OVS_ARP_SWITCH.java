package org.eclipse.tracecompass.analysis.os.linux.ovs.ui.packet;

import static org.eclipse.tracecompass.common.core.NonNullUtils.checkNotNull;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.tracecompass.tmf.core.event.ITmfEvent;
import org.eclipse.tracecompass.tmf.core.event.TmfEvent;
import org.eclipse.tracecompass.tmf.core.request.ITmfEventRequest;
import org.eclipse.tracecompass.tmf.core.request.TmfEventRequest;
import org.eclipse.tracecompass.tmf.core.signal.TmfSignalHandler;
import org.eclipse.tracecompass.tmf.core.signal.TmfTimestampFormatUpdateSignal;
import org.eclipse.tracecompass.tmf.core.signal.TmfTraceSelectedSignal;
import org.eclipse.tracecompass.tmf.core.timestamp.TmfTimeRange;
import org.eclipse.tracecompass.tmf.core.timestamp.TmfTimestampFormat;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.eclipse.tracecompass.tmf.ui.views.TmfView;
import org.swtchart.Chart;
import org.swtchart.ILineSeries;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries.SeriesType;
import org.swtchart.ISeriesSet;
import org.swtchart.LineStyle;
import org.swtchart.Range;

/**
 * @since 2.0
 */
@SuppressWarnings("javadoc")
public class OVS_ARP_SWITCH extends TmfView {

    HashMap<String, ARP_TIME> ARP_PAQUET = new HashMap<>();
    private static final String Y_AXIS_TITLE = "Transfert Time(µs)";
    private static final String X_AXIS_TITLE = "TimeStamp";
    private static final String VIEW_ID = "org.eclipse.tracecompass.analysis.os.linux.ovs.ui.packet";
    private Chart chart;
    private ITmfTrace currentTrace;
    public OVS_ARP_SWITCH() {
        super(VIEW_ID);
        // TODO Auto-generated constructor stub
    }
    @Override
    public void createPartControl(Composite parent) {
        chart = new Chart(parent, SWT.BORDER);
        chart.getTitle().setVisible(false);
        chart.getAxisSet().getXAxis(0).getTitle().setText(X_AXIS_TITLE);
        chart.getAxisSet().getYAxis(0).getTitle().setText(Y_AXIS_TITLE);
        chart.getLegend().setVisible(true);
        chart.getAxisSet().getXAxis(0).getTick().setFormat(new TmfChartTimeStampFormat());
    }
    public class TmfChartTimeStampFormat extends SimpleDateFormat {
        private static final long serialVersionUID = 1L;
        @Override
        public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
            long time = date.getTime();
            toAppendTo.append(TmfTimestampFormat.getDefaulTimeFormat().format(time));
            return toAppendTo;
        }
    }
    /**
     * @param signal
     */
    @TmfSignalHandler
    public void timestampFormatUpdated(TmfTimestampFormatUpdateSignal signal) {
        // Called when the time stamp preference is changed
        chart.getAxisSet().getXAxis(0).getTick().setFormat(new TmfChartTimeStampFormat());
        chart.redraw();
    }
    @Override
    public void setFocus() {
        // TODO Auto-generated method stub
        chart.setFocus();
    }
    @TmfSignalHandler
    public void traceSelected(final TmfTraceSelectedSignal signal) {
        // Don't populate the view again if we're already showing this trace
        if (currentTrace == signal.getTrace()) {
            return;
        }
        currentTrace = signal.getTrace();
        // Create the request to get data from the trace
        TmfEventRequest req = new TmfEventRequest(TmfEvent.class,
                TmfTimeRange.ETERNITY, 0, ITmfEventRequest.ALL_DATA,
                ITmfEventRequest.ExecutionType.BACKGROUND) {
            ArrayList<Double> xValues = new ArrayList<>();
            ArrayList<Double> yValues = new ArrayList<>();
            private double maxY = -Double.MAX_VALUE;
            private double minY = Double.MAX_VALUE;
            private double maxX = -Integer.MAX_VALUE;
            private double minX = Integer.MAX_VALUE;

            @Override
            public void handleData(ITmfEvent data) {
                // Called for each event
                super.handleData(data);
                @NonNull String type = data.getType().getName().toString();
                if(type.equals("addons_lttng_ovs_vport_receive") && data.getContent().getField("protocol").getValue().toString().equals("2048") && !data.getContent().getField("id").getValue().toString().equals("0")) {
                    String paquet_id=data.getContent().getField("id").getValue().toString();
                    long start_time= data.getTimestamp().getValue();
                    ARP_TIME arp=new ARP_TIME();
                    arp.setStart(start_time);
                    ARP_PAQUET.put(paquet_id,arp);
                }if(type.equals("addons_lttng_ovs_vport_send") && data.getContent().getField("protocol").getValue().toString().equals("2048") && !data.getContent().getField("id").getValue().toString().equals("0")){
                    String paquet_id=data.getContent().getField("id").getValue().toString();
                    long paquet_time=data.getTimestamp().getValue();
                    long end_time= data.getTimestamp().getValue();//time
                    if(ARP_PAQUET.containsKey(paquet_id)){
                        long start_time=checkNotNull(ARP_PAQUET.get(checkNotNull(paquet_id))).getStart();
                        long total_time=(end_time-start_time)/ (1000);//value for Y axis

                        xValues.add((double) paquet_time);
                        minX = Math.min(minX,paquet_time);
                        maxX = Math.max(maxX, paquet_time);
                        yValues.add((double) total_time);
                        minY = Math.min(minY, total_time);
                        maxY = Math.max(maxY, total_time);

                    }

                }

            }
            @Override
            public void handleFailure() {
                // Request failed, not more data available
                super.handleFailure();
            }

            @Override
            public void handleSuccess() {
                // Request successful, not more data available
                super.handleSuccess();
                // This part needs to run on the UI thread since it updates the chart SWT control
                Display.getDefault().asyncExec(new Runnable() {

                    @Override
                    public void run() {
                        ISeriesSet seriesSet2 = chart.getSeriesSet();
                        ILineSeries series2=(ILineSeries) seriesSet2.createSeries(SeriesType.LINE,"average_time");
                        series2.setSymbolSize(1);
                        series2.setLineColor(Display.getDefault().getSystemColor(
                                SWT.COLOR_GREEN));
                        series2.setLineStyle(LineStyle.DOT);
                        series2.setLineWidth(4);

                        ISeriesSet seriesSet = chart.getSeriesSet();
                        ILineSeries series=(ILineSeries) seriesSet.createSeries(SeriesType.LINE,"switching_time(arp)");
                        series.setSymbolType(PlotSymbolType.DIAMOND);
                        series.setSymbolSize(1);
                        double x[] = toArray2(xValues);
                        double y[] = toArray(yValues);
                        series.setXSeries(x);
                        series.setYSeries(y);
                        double ye[]=toArrayaverage(yValues);
                        series2.setXSeries(x);
                        series2.setYSeries(ye);
                        if (!xValues.isEmpty() && !yValues.isEmpty()) {
                            chart.getAxisSet().getXAxis(0).setRange(new Range(minX, maxX));
                            chart.getAxisSet().getYAxis(0).setRange(new Range(minY, maxY));
                        } else {
                            chart.getAxisSet().getXAxis(0).setRange(new Range(0, 1));
                            chart.getAxisSet().getYAxis(0).setRange(new Range(0, 1));
                        }
                        chart.getAxisSet().adjustRange();
                        chart.redraw();

                        //System.out.println(chart.getAxisSet().getXAxis(0).getDirection().toString());
                    }

                });
            }

            /**
             * Convert List<Double> to double[]
             */
            private double[] toArray(ArrayList<Double> yValues2) {
                double[] d = new double[yValues2.size()];
                for (int i = 0; i < yValues2.size(); ++i) {
                    d[i] = yValues2.get(i);
                }
                return d;
            }
            private double[] toArray2(ArrayList<Double> xValues2) {
                double[] d = new double[xValues2.size()];
                for (int i = 0; i < xValues2.size(); ++i) {
                    d[i] = xValues2.get(i);
                }
                return d;
            }
            private double[] toArrayaverage(ArrayList<Double> yValues2) {
                double[] d = new double[yValues2.size()];
                double sum = 0,avr;
                for (int i = 0; i < yValues2.size(); ++i) {
                    sum += yValues2.get(i);
                }
                avr=sum/yValues2.size();
                for (int i = 0; i < yValues2.size(); ++i) {
                    d[i] = avr;
                }
                return d;
            }
        };
        ITmfTrace trace = signal.getTrace();
        trace.sendRequest(req);
    }
}
class ARP_TIME{
    long start,end;
    public ARP_TIME(){
        start=0;end=0;
    }
    public void setStart(long s){
        start=s;
    }
    public void setEnd(long e){
        end=e;
    }
    public long getStart(){
        return start;
    }
    public long getEnd(){
        return end;
    }
}
