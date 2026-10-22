package com.zyneonstudios.apex.installer;

public class ApexInstaller {

    private final ApexInstallerFrame frame;

    public ApexInstaller() {
        this.frame = new ApexInstallerFrame();
        frame.launchWindow();
    }

    public ApexInstallerFrame getFrame() {
        return frame;
    }
}