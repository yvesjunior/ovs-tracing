package org.eclipse.tracecompass.analysis.os.linux.ovs.ui.path;

import java.util.Iterator;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.ITimeEvent;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.ITimeGraphEntry;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.TimeGraphEntry;

public class OVSPATHEntry extends TimeGraphEntry implements Comparable<ITimeGraphEntry>{
    public static enum Type {
        /** Null resources (filler rows, etc.) */
        NULL,
        OVS
    }
    private final String fId;
    private final @NonNull ITmfTrace fTrace;
    private final Type fType;
    private final int fQuark;

    public OVSPATHEntry(int quark, @NonNull ITmfTrace trace, String name,
            long startTime, long endTime, Type type, String id) {
        super(name, startTime, endTime);
        fId = id;
        fTrace = trace;
        fType = type;
        fQuark = quark;

    }
    public OVSPATHEntry(@NonNull ITmfTrace trace, String name,
            long startTime, long endTime, String id) {
        this(-1, trace, name, startTime, endTime, Type.NULL, id);
    }

    public OVSPATHEntry(int quark, @NonNull ITmfTrace trace,
            long startTime, long endTime, Type type, String id) {
        this(quark, trace, computeEntryName(type, id), startTime, endTime, type, id);
    }
    private static String computeEntryName(Type type, String id) {
        //return type.toString() + ' ' + id;
        type.toString();
        return id;
    }
    @Override
    public int compareTo(ITimeGraphEntry other) {
        // TODO Auto-generated method stub
        if(!(other instanceof OVSPATHEntry)){
            return -1;
        }
        OVSPATHEntry o=(OVSPATHEntry) other;
        int ret=this.getType().compareTo(o.getType());
        if(ret!=0){
            return ret;
        }
        return this.getId().compareTo(o.getId());
    }

    @Override
    public boolean hasTimeEvents() {
        if (fType == Type.NULL) {
            return false;
        }
        return true;
    }

    @Override
    public Iterator<@NonNull ITimeEvent> getTimeEventsIterator() {
        return super.getTimeEventsIterator();
    }

    public String getId() {
        return fId;
    }

    public ITmfTrace getTrace() {
        return fTrace;
    }

    public Type getType() {
        return fType;
    }

    public int getQuark() {
        return fQuark;
    }

}