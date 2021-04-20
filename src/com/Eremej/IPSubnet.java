package com.Eremej;

public class IPSubnet {
    public final String IP;
    public final String MASK;
    public final int CIDR;

    public IPSubnet(String ip, int cidr) {
        IP = ip;
        CIDR = cidr;
        MASK = SubnetUtils.getMask(cidr);
    }

    public IPSubnet(String ip, String subnet) {
        IP = ip;
        MASK = subnet;
        CIDR = SubnetUtils.getCIDR(this.MASK);
    }
}
