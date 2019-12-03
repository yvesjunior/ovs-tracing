package org.eclipse.tracecompass.analysis.os.linux.ovs.core.path;

import static org.eclipse.tracecompass.common.core.NonNullUtils.checkNotNull;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.tracecompass.tmf.core.statesystem.ITmfStateProvider;
import org.eclipse.tracecompass.tmf.core.statesystem.TmfStateSystemAnalysisModule;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;


/**
 * @author yves
 * @since 2.1
 *
 */
public class OVS_PATH_MODULE extends TmfStateSystemAnalysisModule {
    public static final @NonNull String ID="org.eclipse.tracecompass.analysis.os.linux.ovs.core.module";

    @Override
    protected @NonNull ITmfStateProvider createStateProvider() {
        // TODO Auto-generated method stub
        ITmfTrace trace=checkNotNull(getTrace());
        OVS_PATH_PROVIDER ovs_p=new OVS_PATH_PROVIDER(trace);
        return ovs_p;
    }

    @Override
    protected StateSystemBackendType getBackendType() {
        return StateSystemBackendType.FULL;
    }

}