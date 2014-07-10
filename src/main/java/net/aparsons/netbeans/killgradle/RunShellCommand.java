/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.aparsons.netbeans.killgradle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Build",
        id = "net.aparsons.netbeans.killgradle.RunShellCommand"
)
@ActionRegistration(
        iconBase = "net/aparsons/netbeans/killgradle/skull.png",
        displayName = "#CTL_RunShellCommand"
)
@ActionReferences({
    @ActionReference(path = "Menu/Tools", position = 0),
    @ActionReference(path = "Toolbars/Build", position = 500),
    @ActionReference(path = "Shortcuts", name = "S-F10")
})
@Messages("CTL_RunShellCommand=Kill Gradle")
public class RunShellCommand implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Process process = Runtime.getRuntime().exec("pgrep -f gradle");
            runKillChildrenForEveryLine(process.getInputStream());
            process.waitFor();
        } catch (IOException | InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    private static void runKillChildrenForEveryLine(InputStream is) throws IOException, InterruptedException {
        String pid;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
           while ((pid = reader.readLine()) != null) {
               // Kill all the childeren of the pid
               Runtime.getRuntime().exec("pkill -P " + pid).waitFor();
           } 
        }
    }
    
}
