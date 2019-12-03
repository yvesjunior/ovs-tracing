package org.eclipse.tracecompass.analysis.os.linux.ovs.core.path;

import org.eclipse.tracecompass.statesystem.core.statevalue.ITmfStateValue;
import org.eclipse.tracecompass.statesystem.core.statevalue.TmfStateValue;

/**
 * @since 2.1
 */
@SuppressWarnings("javadoc")
public class StateValues {
    public static int IN_OUT=1;
    public static int OVS_RECEIVE=2;
    public static int OVS_UPCALL=3;
    public static int OVS_SEND=4;

    public static ITmfStateValue OVS_IN_OUT_VALUE=TmfStateValue.newValueInt(IN_OUT);
    public static ITmfStateValue OVS_RECEIVE_VALUE=TmfStateValue.newValueInt(OVS_RECEIVE);
    public static ITmfStateValue OVS_UPCALLS_VALUE=TmfStateValue.newValueInt(OVS_UPCALL);
    public static ITmfStateValue OVS_SEND_VALUE=TmfStateValue.newValueInt(OVS_SEND);


}