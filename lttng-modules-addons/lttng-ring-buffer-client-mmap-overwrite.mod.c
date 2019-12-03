#include <linux/module.h>
#include <linux/vermagic.h>
#include <linux/compiler.h>

MODULE_INFO(vermagic, VERMAGIC_STRING);

__visible struct module __this_module
__attribute__((section(".gnu.linkonce.this_module"))) = {
	.name = KBUILD_MODNAME,
	.init = init_module,
#ifdef CONFIG_MODULE_UNLOAD
	.exit = cleanup_module,
#endif
	.arch = MODULE_ARCH_INIT,
};

static const struct modversion_info ____versions[]
__used
__attribute__((section("__versions"))) = {
	{ 0xcd71858e, __VMLINUX_SYMBOL_STR(module_layout) },
	{ 0xba1d8190, __VMLINUX_SYMBOL_STR(lttng_transport_unregister) },
	{ 0xc1ffd64e, __VMLINUX_SYMBOL_STR(lttng_transport_register) },
	{ 0xe007de41, __VMLINUX_SYMBOL_STR(kallsyms_lookup_name) },
	{ 0x7d1cac90, __VMLINUX_SYMBOL_STR(_lib_ring_buffer_copy_from_user_inatomic) },
	{ 0x26948d96, __VMLINUX_SYMBOL_STR(copy_user_enhanced_fast_string) },
	{ 0xafb8c6ff, __VMLINUX_SYMBOL_STR(copy_user_generic_string) },
	{ 0x72a98fdb, __VMLINUX_SYMBOL_STR(copy_user_generic_unrolled) },
	{ 0x7e66981c, __VMLINUX_SYMBOL_STR(_lib_ring_buffer_strcpy_from_user_inatomic) },
	{ 0x3356b90b, __VMLINUX_SYMBOL_STR(cpu_tss) },
	{ 0x8bdd5b5d, __VMLINUX_SYMBOL_STR(current_task) },
	{ 0x8285477b, __VMLINUX_SYMBOL_STR(lib_ring_buffer_reserve_slow) },
	{ 0x3928efe9, __VMLINUX_SYMBOL_STR(__per_cpu_offset) },
	{ 0x2776a3e5, __VMLINUX_SYMBOL_STR(lib_ring_buffer_nesting) },
	{ 0x7a2af7b4, __VMLINUX_SYMBOL_STR(cpu_number) },
	{ 0xc63d847d, __VMLINUX_SYMBOL_STR(ktime_get_mono_fast_ns) },
	{ 0x53569707, __VMLINUX_SYMBOL_STR(this_cpu_off) },
	{ 0x52e3e0f8, __VMLINUX_SYMBOL_STR(lttng_last_tsc) },
	{ 0xf038f471, __VMLINUX_SYMBOL_STR(lttng_trace_clock) },
	{ 0xaaa1fdf9, __VMLINUX_SYMBOL_STR(_lib_ring_buffer_write) },
	{ 0x69acdf38, __VMLINUX_SYMBOL_STR(memcpy) },
	{ 0xfd141562, __VMLINUX_SYMBOL_STR(_lib_ring_buffer_memset) },
	{ 0xe6dc33c7, __VMLINUX_SYMBOL_STR(_lib_ring_buffer_strcpy) },
	{ 0x16305289, __VMLINUX_SYMBOL_STR(warn_slowpath_null) },
	{ 0xcbf21372, __VMLINUX_SYMBOL_STR(module_put) },
	{ 0x27e1a049, __VMLINUX_SYMBOL_STR(printk) },
	{ 0xe390f06e, __VMLINUX_SYMBOL_STR(try_module_get) },
	{ 0x2cac6861, __VMLINUX_SYMBOL_STR(channel_create) },
	{ 0x9eedc180, __VMLINUX_SYMBOL_STR(channel_destroy) },
	{ 0x2699327c, __VMLINUX_SYMBOL_STR(lib_ring_buffer_open_read) },
	{ 0xfe7c4287, __VMLINUX_SYMBOL_STR(nr_cpu_ids) },
	{ 0xc0a3d105, __VMLINUX_SYMBOL_STR(find_next_bit) },
	{ 0x9ad02601, __VMLINUX_SYMBOL_STR(lib_ring_buffer_release_read) },
	{ 0x3da5af9f, __VMLINUX_SYMBOL_STR(lib_ring_buffer_offset_address) },
	{ 0xb669eeab, __VMLINUX_SYMBOL_STR(channel_get_ring_buffer) },
	{ 0x4854f9cf, __VMLINUX_SYMBOL_STR(lib_ring_buffer_read_offset_address) },
	{ 0xbdfb6dbb, __VMLINUX_SYMBOL_STR(__fentry__) },
};

static const char __module_depends[]
__used
__attribute__((section(".modinfo"))) =
"depends=lttng-tracer,lttng-lib-ring-buffer,lttng-clock";


MODULE_INFO(srcversion, "BF91EA63580D271A0F5CDE8");
