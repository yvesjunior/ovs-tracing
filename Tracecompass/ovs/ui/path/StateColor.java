package org.eclipse.tracecompass.analysis.os.linux.ovs.ui.path;

import org.eclipse.swt.graphics.RGB;

public class StateColor {
    public enum State {
        IN_OUT (new RGB(0,29,252)),
        RECEIVE (new RGB(0,252,252)),
        UPCALL (new RGB(255, 0, 0)),
        SEND (new RGB(13,255,0)),
        DEFAULT (new RGB(250,247,247));

        public final RGB rgb;

        private State(RGB rgb) {
            this.rgb = rgb;
        }
    }
}