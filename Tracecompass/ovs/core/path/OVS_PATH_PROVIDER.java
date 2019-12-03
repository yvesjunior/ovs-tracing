package org.eclipse.tracecompass.analysis.os.linux.ovs.core.path;
import static org.eclipse.tracecompass.common.core.NonNullUtils.checkNotNull;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.tracecompass.statesystem.core.ITmfStateSystemBuilder;
import org.eclipse.tracecompass.statesystem.core.statevalue.ITmfStateValue;
import org.eclipse.tracecompass.statesystem.core.statevalue.TmfStateValue;
import org.eclipse.tracecompass.tmf.core.event.ITmfEvent;
import org.eclipse.tracecompass.tmf.core.statesystem.AbstractTmfStateProvider;
import org.eclipse.tracecompass.tmf.core.statesystem.ITmfStateProvider;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;

/**
 * @author yves
 * @since 2.1
 *
 */
public class OVS_PATH_PROVIDER extends AbstractTmfStateProvider {
    public static final String ID="ovs.trace.path.provider.id"; //$NON-NLS-1$
    public static final String RECEIVE="addons_lttng_ovs_vport_receive";
    public static final String UPCALL_START="addons_lttng_ovs_upcall_start";
    public static final String UPCALL_END="addons_lttng_ovs_upcall_end";
    public static final String SEND="addons_lttng_ovs_vport_send";

    public OVS_PATH_PROVIDER(@NonNull ITmfTrace trace) {
        super(trace, ID);
        // TODO Auto-generated constructor stub
    }

    @Override
    public int getVersion() {
        // TODO Auto-generated method stub
        return 1;
    }

    @Override
    public @NonNull ITmfStateProvider getNewInstance() {
        // TODO Auto-generated method stub
        return new OVS_PATH_PROVIDER(getTrace());
    }

    @Override
    protected void eventHandle(@NonNull ITmfEvent ev) {
        // TODO Auto-generated method stub

        ITmfEvent event=ev;
        final long ts=event.getTimestamp().getValue();
        ITmfStateValue value=TmfStateValue.nullValue();
        ITmfStateSystemBuilder ss = checkNotNull(getStateSystemBuilder());
        int quark;
        if(event.getName().contains("addons_lttng_ovs")) {
            if(!event.getContent().getField("id").getValue().toString().equals("0")){
                switch (event.getType().getName()) {
                case RECEIVE:
                    quark=ss.getQuarkAbsoluteAndAdd("OVS", "IN_OUT","State");
                    value=StateValues.OVS_IN_OUT_VALUE;
                    ss.modifyAttribute(ts,value,quark);

                    quark=ss.getQuarkAbsoluteAndAdd("OVS", "PACKET_PROCESSING","State");
                    value=StateValues.OVS_RECEIVE_VALUE;
                    ss.modifyAttribute(ts, value, quark);
                    break;
                case UPCALL_START:
                    quark=ss.getQuarkAbsoluteAndAdd("OVS", "PACKET_PROCESSING","State");
                    value=TmfStateValue.nullValue();
                    ss.modifyAttribute(ts, value, quark);

                    quark=ss.getQuarkAbsoluteAndAdd("OVS", "PACKET_PROCESSING","State");
                    value=StateValues.OVS_UPCALLS_VALUE;
                    ss.modifyAttribute(ts, value, quark);



                    break;
                case UPCALL_END:
                    quark=ss.getQuarkAbsoluteAndAdd("OVS", "PACKET_PROCESSING","State");
                    value=TmfStateValue.nullValue();
                    ss.modifyAttribute(ts, value, quark);

                    quark= ss.getQuarkAbsoluteAndAdd("OVS", "PACKET_PROCESSING","State");
                    value=StateValues.OVS_SEND_VALUE;
                    ss.modifyAttribute(ts,value,quark);
                    break;
                case SEND:
                    quark=ss.getQuarkAbsoluteAndAdd("OVS", "IN_OUT","State");
                    value=TmfStateValue.nullValue();
                    ss.modifyAttribute(ts,value,quark);

                    quark= ss.getQuarkAbsoluteAndAdd("OVS", "PACKET_PROCESSING","State");
                    value=TmfStateValue.nullValue();
                    ss.modifyAttribute(ts,value,quark);


                    break;

                default:
                    break;
                }

            }
        }
    }

}
