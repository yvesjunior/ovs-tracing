sudo modprobe nf_conntrack
sudo modprobe nf_nat
sudo modprobe nf_defrag_ipv6
sudo modprobe libcrc32c
sudo modprobe nf_nat_ipv6
sudo modprobe nf_nat_ipv4
sudo modprobe gre
sudo rmmod openvswitch
sudo insmod ./datapath/linux/openvswitch.ko


