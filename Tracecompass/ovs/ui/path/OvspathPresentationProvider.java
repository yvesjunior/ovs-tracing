package org.eclipse.tracecompass.analysis.os.linux.ovs.ui.path;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.tracecompass.analysis.os.linux.ovs.core.path.OVS_PATH_MODULE;
import org.eclipse.tracecompass.analysis.os.linux.ovs.core.path.StateValues;
import org.eclipse.tracecompass.analysis.os.linux.ovs.ui.path.OVSPATHEntry.Type;
import org.eclipse.tracecompass.internal.analysis.os.linux.ui.Activator;
import org.eclipse.tracecompass.statesystem.core.ITmfStateSystem;
import org.eclipse.tracecompass.statesystem.core.exceptions.TimeRangeException;
import org.eclipse.tracecompass.tmf.core.statesystem.TmfStateSystemAnalysisModule;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.StateItem;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.TimeGraphPresentationProvider;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.ITimeEvent;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.NullTimeEvent;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.TimeEvent;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.widgets.Utils;
public class OvspathPresentationProvider extends TimeGraphPresentationProvider {
    private Color fColorWhite;
    private Color fColorGray;
    private Integer fAverageCharWidth;

    public OvspathPresentationProvider(){
        super();
    }
    private static StateColor.State[] getStateValues() {
        return StateColor.State.values();
    }
    private static StateColor.State getEventState(TimeEvent event) {
        if (event.hasValue()) {
            OVSPATHEntry entry = (OVSPATHEntry) event.getEntry();
            int value = event.getValue();
            if (entry.getType() == Type.OVS) {//entry type SERVICE
                if (value == StateValues.IN_OUT ) {
                    return StateColor.State.IN_OUT;
                }
                else if(value == StateValues.OVS_RECEIVE){
                    return StateColor.State.RECEIVE;

                }else if(value == StateValues.OVS_UPCALL){
                    return StateColor.State.UPCALL;
                }else if(value == StateValues.OVS_SEND){
                    return StateColor.State.SEND;
                }else{
                    return StateColor.State.DEFAULT;
                }
            }
        }
        return null;
    }
    @Override
    public int getStateTableIndex(ITimeEvent event) {
        StateColor.State state = getEventState((TimeEvent) event);
        if (state != null) {
            return state.ordinal();
        }
        if (event instanceof NullTimeEvent) {
            return INVISIBLE;
        }
        return TRANSPARENT;
    }

    @Override
    public StateItem[] getStateTable() {
        StateColor.State[] states = getStateValues();
        StateItem[] stateTable = new StateItem[states.length];
        for (int i = 0; i < stateTable.length; i++) {
            StateColor.State state = states[i];
            stateTable[i] = new StateItem(state.rgb, state.toString());
        }
        return stateTable;
    }

    @Override
    public String getEventName(ITimeEvent event) {
        StateColor.State state = getEventState((TimeEvent) event);
        if (state != null) {
            return state.toString();
        }
        if (event instanceof NullTimeEvent) {
            return null;
        }
        return ""; //$NON-NLS-1$
    }

    @Override
    public void postDrawEvent(ITimeEvent event, Rectangle bounds, GC gc) {
        if (fAverageCharWidth == null) {
            fAverageCharWidth = gc.getFontMetrics().getAverageCharWidth();
        }
        if (bounds.width <= fAverageCharWidth) {
            return;
        }
        //        if (!(event instanceof TimeEvent)) {
        //            return;
        //        }

        OVSPATHEntry entry = (OVSPATHEntry) event.getEntry();
        @SuppressWarnings("null")
        ITmfStateSystem ss = TmfStateSystemAnalysisModule.getStateSystem(entry.getTrace(), OVS_PATH_MODULE.ID);
        if (ss == null) {
            return;
        }
        try {
            //String str=ss.querySingleState(event.getTime(), entry.getQuark()+2).getStateValue().unboxStr();
          //  String str=event.getEntry().getName();
//            if(str.equals("nullValue")) {
//                str="";
//            }else{str=":"+str;}
            gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_WHITE));
            String state= getEventName(event);
            //Utils.drawText(gc, state+str, bounds.x, bounds.y, bounds.width, bounds.height, true, true);
            Utils.drawText(gc, state, bounds.x, bounds.y, bounds.width, bounds.height, true, true);

        } catch (TimeRangeException e) {
            Activator.getDefault().logError("Error in ControlFlowPresentationProvider", e); //$NON-NLS-1$
        }
    }

    public Color getColorWhite() {
        return fColorWhite;
    }
    public void setColorWhite(Color colorWhite) {
        fColorWhite = colorWhite;
    }
    public Color getColorGray() {
        return fColorGray;
    }
    public void setColorGray(Color colorGray) {
        fColorGray = colorGray;
    }
    public Integer getAverageCharWidth() {
        return fAverageCharWidth;
    }
    public void setAverageCharWidth(Integer averageCharWidth) {
        fAverageCharWidth = averageCharWidth;
    }
}