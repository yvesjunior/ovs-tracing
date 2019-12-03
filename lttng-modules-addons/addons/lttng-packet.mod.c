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
	{ 0xe007de41, __VMLINUX_SYMBOL_STR(kallsyms_lookup_name) },
	{ 0x9729042e, __VMLINUX_SYMBOL_STR(nf_unregister_hooks) },
	{ 0x27e1a049, __VMLINUX_SYMBOL_STR(printk) },
	{ 0x54ad48aa, __VMLINUX_SYMBOL_STR(nf_register_hooks) },
	{ 0x25925e18, __VMLINUX_SYMBOL_STR(__inet_lookup_listener) },
	{ 0xcfa21a7e, __VMLINUX_SYMBOL_STR(__inet_lookup_established) },
	{ 0xd92e2dca, __VMLINUX_SYMBOL_STR(tcp_hashinfo) },
	{ 0x8ee23329, __VMLINUX_SYMBOL_STR(sk_free) },
	{ 0xbd100793, __VMLINUX_SYMBOL_STR(cpu_online_mask) },
	{ 0x7a2af7b4, __VMLINUX_SYMBOL_STR(cpu_number) },
	{ 0xbdfb6dbb, __VMLINUX_SYMBOL_STR(__fentry__) },
};

static const char __module_depends[]
__used
__attribute__((section(".modinfo"))) =
"depends=";


MODULE_INFO(srcversion, "46268A38911FD434426EEE3");
