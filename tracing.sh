#!/bin/sh


lttng create
#tracing ovs
#lttng enable-event -k inet_sock_local_in   
#lttng enable-event -k inet_sock_local_out
lttng enable-event -k addons_lttng_ovs_vport_receive
lttng enable-event -k addons_lttng_ovs_upcall_start
lttng enable-event -k addons_lttng_ovs_upcall_end
lttng enable-event -k addons_lttng_ovs_vport_send
#tracing python scripts
lttng enable-event -p -a
lttng start


