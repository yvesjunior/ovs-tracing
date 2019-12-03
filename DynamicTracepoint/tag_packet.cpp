// This script is original proposed by Mick Tarsel
// Some modifications have been done do handle LTTng addons dynamique tracepoint
struct packet_tag {
	char eyeCatch;
	unsigned long reqid;
};

#define TAGGED 0x69

static inline int tag_packet(struct sk_buff *skb)
{
	struct packet_tag pkt_id;
	static unsigned int id;
	unsigned char *temp_pkt;
//----------------	
	struct packet_tag *pkt_id1;
	unsigned char *temp_pkt1;
	
	temp_pkt1 = (skb->data)+(skb->len) - sizeof(struct packet_tag);
	pkt_id1 = (struct packet_tag *)temp_pkt1;

	if (pkt_id1->eyeCatch != TAGGED) {
		pkt_id.eyeCatch = TAGGED;
		pkt_id.reqid = ++id;

		if (sizeof(struct packet_tag) <= skb_tailroom(skb)) {
			temp_pkt = skb_put(skb, sizeof(pkt_id));
			memcpy(temp_pkt, &pkt_id, sizeof(pkt_id));
			pr_debug("%s:skb=%p reqid=%lu\n", __func__, skb, pkt_id.reqid);
		return id;
		}
	}	
		

	

	if (net_ratelimit())
		pr_debug("%s:Insufficient room to tag skb=%p\n", __func__, skb);

	return 0;
}

static inline int report_packet(struct sk_buff *skb)
{
	struct packet_tag *pkt_id;
	unsigned char *temp_pkt;

	temp_pkt = (skb->data)+(skb->len) - sizeof(struct packet_tag);
	pkt_id = (struct packet_tag *)temp_pkt;

	if (pkt_id->eyeCatch == TAGGED) {
		pr_debug("%s:skb=%p reqid=%lu\n", __func__, skb, pkt_id->reqid);
		return pkt_id->reqid;
	}

	return 0;
}
