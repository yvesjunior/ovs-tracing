/*
 * addons/lttng-elv.c
 *
 * Missing tracepoint for recovering the block device request chain
 *
 * Copyright (C) 2015 Mathieu Desnoyers <mathieu.desnoyers@efficios.com>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; only
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
#include <linux/netdevice.h>
#include <linux/module.h>
#include <linux/printk.h>
#include <linux/kprobes.h>
#include <linux/blkdev.h>
#include <linux/spinlock.h>
#include <linux/skbuff.h>
#include <net/genetlink.h>
#include "../wrapper/tracepoint.h"
#include "../lttng-abi.h"
#define LTTNG_INSTRUMENTATION
#include "../instrumentation/events/lttng-module/addons.h"

DEFINE_TRACE(addons_lttng_ovs_upcall_end);

static int
lttng_ovs_upcall_end_probe(struct sk_buff *skb, struct genl_info *info)
{
        trace_addons_lttng_ovs_upcall_end(1);
	jprobe_return();
	return 0;
}

static struct jprobe lttng_ovs_upcall_end_jprobe = {
		.entry = lttng_ovs_upcall_end_probe,
		.kp = {
			.symbol_name = "ovs_packet_cmd_execute",
		},
};

static int __init addons_lttng_ovs_upcall_end_init(void)
{
	int ret;

//	(void) wrapper_lttng_fixup_sig(THIS_MODULE);
	ret = register_jprobe(&lttng_ovs_upcall_end_jprobe);
	if (ret < 0) {
		printk("register_jprobe failed, returned %d\n", ret);
		goto out;
	}

	printk("lttng-ovs-upcall-end-probe loaded\n");
out:
	return ret;
}
module_init(addons_lttng_ovs_upcall_end_init);

static void __exit addons_lttng_ovs_upcall_end_exit(void)
{
	unregister_jprobe(&lttng_ovs_upcall_end_jprobe);
	printk("lttng-ovs-upcall-end-probe removed\n");
}
module_exit(addons_lttng_ovs_upcall_end_exit);

MODULE_LICENSE("GPL and additional rights");
MODULE_AUTHOR("Yves  <Yves.@gmail.com>");
MODULE_DESCRIPTION("LTTng openVswitch event");

