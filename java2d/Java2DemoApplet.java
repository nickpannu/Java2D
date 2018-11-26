/*
 *
 * Copyright (c) 2007, 2011, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package java2d;

import static java2d.Java2Demo.DemoProgress;
import static java2d.RunWindow.RunWindowSettings;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;


/**
 * A demo that shows Java2D features.
 *
 * Parameters that can be used in the Java2Demo.html file inside
 * the applet tag to customize demo runs :
<param name="runs" value="10">
<param name="delay" value="10">
<param name="ccthread" value=" ">
<param name="screen" value="5">
<param name="antialias" value="true">
<param name="rendering" value="true">
<param name="texture" value="true">
<param name="composite" value="true">
<param name="verbose" value=" ">
<param name="buffers" value="3,10">
<param name="verbose" value=" ">
<param name="zoom" value=" ">
 *
 * @author Brian Lichtenwalter  (Framework, Intro, demos)
 * @author Jim Graham           (demos)
 * @author Alexander Kouznetsov (code beautification)
 */
@SuppressWarnings("serial")
public class Java2DemoApplet extends JApplet {
    private Java2Demo demo;

    @Override
    public void init() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    JPanel panel = new JPanel();
                    getContentPane().add(panel, BorderLayout.CENTER);
                    panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

                    JPanel progressPanel = new JPanel() {

                        @Override
                        public Insets getInsets() {
                            return new Insets(40, 30, 20, 30);
                        }
                    };
                    progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));

                    panel.add(Box.createGlue());
                    panel.add(progressPanel);
                    panel.add(Box.createGlue());

                    progressPanel.add(Box.createGlue());

                    Dimension d = new Dimension(400, 20);
                    JLabel progressLabel = new JLabel("Loading, please wait...");
                    progressLabel.setMaximumSize(d);
                    progressPanel.add(progressLabel);
                    progressPanel.add(Box.createRigidArea(new Dimension(1, 20)));

                    JProgressBar progressBar = new JProgressBar();
                    progressBar.setStringPainted(true);
                    progressLabel.setLabelFor(progressBar);
                    progressBar.setAlignmentX(CENTER_ALIGNMENT);
                    progressBar.setMaximumSize(d);
                    progressBar.setMinimum(0);
                    progressBar.setValue(0);
                    progressPanel.add(progressBar);
                    progressPanel.add(Box.createGlue());
                    progressPanel.add(Box.createGlue());
                    DemoProgress demoProgress = new DemoProgress(progressLabel, progressBar);

                    Rectangle ab = getContentPane().getBounds();
                    panel.setPreferredSize(new Dimension(ab.width, ab.height));
                    getContentPane().add(panel, BorderLayout.CENTER);
                    validate();
                    setVisible(true);

                    RunWindowSettings runWndSetts = new RunWindowSettings();
                    demo = new Java2Demo(true, demoProgress, runWndSetts);
                    getContentPane().remove(panel);
                    getContentPane().setLayout(new BorderLayout());
                    getContentPane().add(demo, BorderLayout.CENTER);

                    String param = null;

                    if ((param = getParameter("delay")) != null) {
                        runWndSetts.setDelay(Integer.parseInt(param));
                    }
                    if (getParameter("ccthread") != null) {
                        demo.getCcthreadCB().setSelected(true);
                    }
                    if ((param = getParameter("screen")) != null) {
                        demo.getControls().screenCombo.setSelectedIndex(Integer.parseInt(param));
                    }
                    if ((param = getParameter("antialias")) != null) {
                        demo.getControls().aliasCB.setSelected(param.endsWith("true"));
                    }
                    if ((param = getParameter("rendering")) != null) {
                        demo.getControls().renderCB.setSelected(param.endsWith("true"));
                    }
                    if ((param = getParameter("texture")) != null) {
                        demo.getControls().textureCB.setSelected(param.endsWith("true"));
                    }
                    if ((param = getParameter("composite")) != null) {
                        demo.getControls().compositeCB.setSelected(param.endsWith("true"));
                    }
                    if (getParameter("verbose") != null) {
                        demo.getVerboseCB().setSelected(true);
                    }
                    if ((param = getParameter("columns")) != null) {
                        demo.setGroupColumns(Integer.parseInt(param));
                    }
                    if ((param = getParameter("buffers")) != null) {
                        // usage -buffers=3,10
                        runWndSetts.setBuffersFlag(true);
                        int i = param.indexOf(',');
                        String s1 = param.substring(0, i);
                        runWndSetts.setBufBeg(Integer.parseInt(s1));
                        s1 = param.substring(i + 1, param.length());
                        runWndSetts.setBufEnd(Integer.parseInt(s1));
                    }
                    if (getParameter("zoom") != null) {
                        runWndSetts.setZoomCBSelected(true);
                    }
                    if ((param = getParameter("runs")) != null) {
                        runWndSetts.setNumRuns(Integer.parseInt(param));
                        demo.createRunWindow();
                        demo.startRunWindow();
                    }
                    validate();
                    repaint();
                    requestDefaultFocus();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestDefaultFocus() {
        Container nearestRoot = getFocusCycleRootAncestor();
        if (nearestRoot != null) {
            nearestRoot.getFocusTraversalPolicy().getDefaultComponent(
                    nearestRoot).requestFocus();
        }
    }

    @Override
    public void start() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    if (demo != null) {
                        demo.start();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    if (demo != null) {
                        demo.stop();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    getContentPane().removeAll();
                    validate();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
