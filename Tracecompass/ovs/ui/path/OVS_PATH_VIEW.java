package org.eclipse.tracecompass.analysis.os.linux.ovs.ui.path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.tracecompass.analysis.os.linux.ovs.core.path.*;
import org.eclipse.tracecompass.analysis.os.linux.ovs.ui.path.OVSPATHEntry.Type;
import org.eclipse.tracecompass.statesystem.core.ITmfStateSystem;
import org.eclipse.tracecompass.statesystem.core.exceptions.AttributeNotFoundException;
import org.eclipse.tracecompass.statesystem.core.interval.ITmfStateInterval;
import org.eclipse.tracecompass.tmf.core.statesystem.TmfStateSystemAnalysisModule;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.eclipse.tracecompass.tmf.ui.views.timegraph.AbstractStateSystemTimeGraphView;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.ITimeEvent;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.ITimeGraphEntry;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.NullTimeEvent;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.TimeEvent;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.TimeGraphEntry;
import static org.eclipse.tracecompass.common.core.NonNullUtils.checkNotNull;

public class OVS_PATH_VIEW extends  AbstractStateSystemTimeGraphView  {
    private static final long BUILD_UPDATE_TIMEOUT = 500;
    public static final String ID = "org.eclipse.tracecompass.analysis.os.linux.ovs.ui.path"; //$NON-NLS-1$

    private static final String[] COLUMN_NAMES = new String[] {
            "PATH"
    };
    static OvspathPresentationProvider sp=new OvspathPresentationProvider();

    public OVS_PATH_VIEW() {
        super(ID,sp);
        // TODO Auto-generated constructor stub
        setTreeColumns(COLUMN_NAMES);
        setFilterLabelProvider(new OVSFilterLabelProvider());
        setTreeLabelProvider(new OVSTreeLabelProvider());
    }
    private static class OVSFilterLabelProvider extends TreeLabelProvider {
        @Override
        public String getColumnText(Object element, int columnIndex) {
            OVSPATHEntry entry = (OVSPATHEntry) element;
            if (columnIndex == 0) {
                return entry.getName();
            }
            return "test";
        }
    }
    /**
     * @author yves
     *
     */
    protected static class OVSTreeLabelProvider extends TreeLabelProvider {

        @Override
        public String getColumnText(Object element, int columnIndex) {
            OVSPATHEntry entry = (OVSPATHEntry) element;
            if (columnIndex==0) {
                return entry.getName();
            }
            return "yy";
        }
    }
    @Override
    protected @Nullable List<ITimeEvent> getEventList(@NonNull TimeGraphEntry entry, ITmfStateSystem ssq, @NonNull List<List<ITmfStateInterval>> fullStates, @Nullable List<ITmfStateInterval> prevFullState, @NonNull IProgressMonitor monitor) {
        // TODO Auto-generated method stub
        List<ITimeEvent> eventList = null;
        OVSPATHEntry resourcesEntry = (OVSPATHEntry) entry;
        try {
            if (resourcesEntry.getType().equals(Type.OVS)) {
                int quark=resourcesEntry.getQuark();
                int statusQuark=ssq.getQuarkRelative(quark, "State");
                System.out.println(statusQuark);
                eventList = new ArrayList<>(fullStates.size());
                ITmfStateInterval lastInterval = prevFullState == null || statusQuark >= prevFullState.size() ? null : prevFullState.get(statusQuark);
                long lastStartTime = lastInterval == null ? -1 : lastInterval.getStartTime();
                long lastEndTime = lastInterval == null ? -1 : lastInterval.getEndTime() + 1;
                for (List<ITmfStateInterval> fullState : fullStates) {
                    if (monitor.isCanceled()) {
                        return null;
                    }
                    if (statusQuark >= fullState.size()) {
                        // No information on this CPU (yet?), skip it for now
                        continue;
                    }
                    ITmfStateInterval statusInterval = fullState.get(statusQuark);
                    int status = statusInterval.getStateValue().unboxInt();
                    long time = statusInterval.getStartTime();
                    long duration = statusInterval.getEndTime() - time + 1;
                    if (time == lastStartTime) {
                        continue;
                    }
                    if (!statusInterval.getStateValue().isNull()) {
                        if (lastEndTime != time && lastEndTime != -1) {
                            eventList.add(new TimeEvent(entry, lastEndTime, time - lastEndTime));
                        }
                        eventList.add(new TimeEvent(entry, time, duration, status));
                        //System.out.println(new TimeEvent(entry, time, duration, status));
                    } else {
                        eventList.add(new NullTimeEvent(entry, time, duration));
                    }
                    lastStartTime = time;
                    lastEndTime = time + duration;
                }
            }
        } catch (AttributeNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return eventList;
    }
    @Override
    protected void buildEntryList(@NonNull ITmfTrace trace, @NonNull ITmfTrace parentTrace, @NonNull IProgressMonitor monitor) {
        // TODO Auto-generated method stub
        final ITmfStateSystem ssq = TmfStateSystemAnalysisModule.getStateSystem(trace, OVS_PATH_MODULE.ID);
        if (ssq == null) {
            return;
        }
        Comparator<ITimeGraphEntry> comparator = new Comparator<ITimeGraphEntry>() {
            @Override
            public int compare(ITimeGraphEntry o1, ITimeGraphEntry o2) {
                return ((OVSPATHEntry) o1).compareTo(o2);
            }
        };
        Map<Integer, OVSPATHEntry> entryMap = new HashMap<>();
        TimeGraphEntry traceEntry = null;
        long startTime = ssq.getStartTime();
        long start = startTime;
        setStartTime(Math.min(getStartTime(), startTime));
        boolean complete = false;
        while (!complete) {
            if (monitor.isCanceled()) {
                return;
            }
            complete = ssq.waitUntilBuilt(BUILD_UPDATE_TIMEOUT);
            if (ssq.isCancelled()) {
                return;
            }
            long end = ssq.getCurrentEndTime();
            if (start == end && !complete) { // when complete execute one last time regardless of end time
                continue;
            }
            long endTime = end + 1;
            setEndTime(Math.max(getEndTime(), endTime));
            if (traceEntry == null) {
                traceEntry = new OVSPATHEntry(trace, trace.getName(), startTime, endTime, "OVS");
                traceEntry.sortChildren(comparator);
                List<TimeGraphEntry> entryList = Collections.singletonList(traceEntry);
                addToEntryList(parentTrace, ssq, entryList);
            } else {
                traceEntry.updateEndTime(endTime);
            }//---------
            List<Integer> sQuarks = ssq.getQuarks("OVS", "*"); //$NON-NLS-1$
            for (Integer serviceQuark : sQuarks) {
                String serv = ssq.getAttributeName(serviceQuark).toString();
                @Nullable OVSPATHEntry entry = entryMap.get(serviceQuark);
                if(serv!="PACKET_PROCESSING"){
                    if (entry == null) {
                        entry = new OVSPATHEntry(serviceQuark, trace, startTime, endTime, Type.OVS, serv);
                        entryMap.put(serviceQuark, entry);
                        traceEntry.addChild(entry);

                    }
                    else {
                        entry.updateEndTime(endTime);
                    }
                }else{
                    if (entry == null) {
                        if(serv=="PACKET_PROCESSING") {
                            @Nullable OVSPATHEntry entry1  = checkNotNull(entryMap.get(serviceQuark-2));
                            entry1.addChild(new OVSPATHEntry(serviceQuark, trace, startTime, endTime, Type.OVS, serv));
                        }
                    }
                    else {
                        entry.updateEndTime(endTime);
                    }
                    }
                    // }
                }

                if (parentTrace.equals(getTrace())) {
                    refresh();
                }
                final List<? extends ITimeGraphEntry> traceEntryChildren = traceEntry.getChildren();
                final long resolution = Math.max(1, (endTime - ssq.getStartTime()) / getDisplayWidth());
                final long qStart = start;
                final long qEnd = end;
                queryFullStates(ssq, qStart, qEnd, resolution, monitor, new IQueryHandler() {
                    @Override
                    public void handle(List<List<ITmfStateInterval>> fullStates, List<ITmfStateInterval> prevFullState) {
                        for (ITimeGraphEntry child : traceEntryChildren) {
                            if (monitor.isCanceled()) {
                                return;
                            }
                            if (child instanceof TimeGraphEntry) {
                                TimeGraphEntry entry = (TimeGraphEntry) child;
                                List<ITimeEvent> eventList = getEventList(entry, ssq, fullStates, prevFullState, monitor);
                                if (eventList != null) {
                                    for (ITimeEvent event : eventList) {
                                        entry.addEvent(event);
                                    }
                                }
                            }
                        }
                    }
                });

                start = end;
            }
        }
    }