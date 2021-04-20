package com.Eremej;

import java.util.List;

/* An example of a .bat file command: route ADD 157.0.0.0 MASK 255.0.0.0 157.55.80.1 */
public abstract class OutBuilders {
    public static OutBuilder getWindowsBATShortBuilder() {
        return subnetList -> {
            StringBuilder outputBuilder = new StringBuilder();

            for (IPSubnet subnet : subnetList)
                outputBuilder.append("route ADD " + subnet.IP + " MASK " + subnet.MASK + " 0.0.0.0" + "\n");

            return outputBuilder.toString();
        };
    }

    public static OutBuilder getWindowsADDParametrisedBuilder(String[] args) {
        return subnetList -> {
            StringBuilder outputBuilder = new StringBuilder();

            for (IPSubnet subnet : subnetList) {
                outputBuilder.append("route ADD " + subnet.IP + " MASK " + subnet.MASK);
                for (int i = 0; i < args.length; i++)
                    outputBuilder.append(" " + args[i]);
                outputBuilder.append("\n");
            }

            return outputBuilder.toString();
        };
    }
}
