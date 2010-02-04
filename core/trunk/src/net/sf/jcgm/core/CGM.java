/*
 * <copyright> Copyright 1997-2003 BBNT Solutions, LLC under sponsorship of the
 * Defense Advanced Research Projects Agency (DARPA).
 * Copyright 2009 Swiss AviationSoftware Ltd.
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the Cougaar Open Source License as published by DARPA on
 * the Cougaar Open Source Website (www.cougaar.org).
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package net.sf.jcgm.core;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.BufferedInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import net.sf.jcgm.core.ScalingMode.Mode;

/**
 * The main class for Computer Graphics Metafile support.
 * @author xphc (Philippe Cadé)
 * @author BBNT Solutions
 * @version $Id$
 */
public class CGM implements Cloneable {
    private List<Command> commands;

    private final List<ICommandListener> commandListeners = new ArrayList<ICommandListener>();
    
    private final static int INITIAL_NUM_COMMANDS = 500;

    public CGM() {
    	// empty constructor. XXX: Remove?
    }

	public CGM(File cgmFile) throws IOException {
		if (cgmFile == null)
			throw new NullPointerException("unexpected null parameter");

		InputStream inputStream;
		String cgmFilename = cgmFile.getName();
		if (cgmFilename.endsWith(".cgm.gz") || cgmFilename.endsWith(".cgmz")) {
			inputStream = new GZIPInputStream(new FileInputStream(cgmFile));
		}
		else {
			inputStream = new FileInputStream(cgmFile);
		}
        DataInputStream in = new DataInputStream(new BufferedInputStream(inputStream));
        read(in);
        in.close();
    }

    public void read(DataInput in) throws IOException {
    	reset();
        this.commands = new ArrayList<Command>(INITIAL_NUM_COMMANDS);
        while (true) {
            Command c = Command.read(in);
            if (c == null)
                break;

            for (ICommandListener listener : this.commandListeners) {
				listener.commandProcessed(c.getElementClass(), c.getElementCode(), c.toString());
			}

            // get rid of all arguments after we read them
            c.cleanUpArguments();
            this.commands.add(c);
        }
    }

    /**
     * Adds the given listener to the list of command listeners
     * @param listener The listener to add
     */
    public void addCommandListener(ICommandListener listener) {
    	this.commandListeners.add(listener);
    }

    /**
     * All the command classes with static data need to be reset here
     */
	private void reset() {
		ColorIndexPrecision.reset();
		ColorModel.reset();
		ColorPrecision.reset();
		ColorSelectionMode.reset();
		ColourValueExtent.reset();
		EdgeWidthSpecificationMode.reset();
		IndexPrecision.reset();
		IntegerPrecision.reset();
		LineWidthSpecificationMode.reset();
		MarkerSizeSpecificationMode.reset();
		RealPrecision.reset();
		RestrictedTextType.reset();
		VDCIntegerPrecision.reset();
		VDCRealPrecision.reset();
		VDCType.reset();

		Messages.getInstance().reset();
	}

	public List<Message> getMessages() {
		return Messages.getInstance();
	}

	public void paint(CGMDisplay d) {
		for (Command c : this.commands) {
			if (filter(c)) {
				c.paint(d);
			}
		}
    }

	private boolean filter(Command c) {
		return true;
//		List<Class<?>> classes = new ArrayList<Class<?>>();
//		//classes.add(PolygonElement.class);
//		classes.add(Text.class);
//		//classes.add(CircleElement.class);
//		
//		for (Class<?> clazz: classes) {
//			if (clazz.isInstance(c))
//				return false;
//		}
//		
//		return true;
	}

    /**
     * Returns the size of the CGM graphic.
     * @return The dimension or null if no {@link VDCExtent} command was found.
     */
    public Dimension getSize() {
    	// default to 96 DPI which is the Microsoft Windows default DPI setting
    	return getSize(96);
    }

    /**
     * Returns the size of the CGM graphic taking into account a specific DPI setting
     * @param dpi The DPI value to use
     * @return The dimension or null if no {@link VDCExtent} command was found.
     */
    public Dimension getSize(double dpi) {
    	Point2D.Double[] extent = extent();
    	if (extent == null)
    		return null;

    	double factor = 1;

    	ScalingMode scalingMode = getScalingMode();
    	if (scalingMode != null) {
    		Mode mode = scalingMode.getMode();
    		if (ScalingMode.Mode.METRIC.equals(mode)) {
    			double metricScalingFactor = scalingMode.getMetricScalingFactor();
    			if (metricScalingFactor != 0) {
    				// 1 inch = 25,4 millimeter
    				factor = (dpi * metricScalingFactor) / 25.4;
    			}
    		}
    	}

    	int width = (int)(Math.abs(extent[1].x - extent[0].x) * factor);
    	int height = (int)(Math.abs(extent[1].y - extent[0].y) * factor);

    	return new Dimension(width, height);
    }

    public Point2D.Double[] extent() {
    	for (Command c : this.commands) {
            if (c instanceof VDCExtent) {
                Point2D.Double[] extent = ((VDCExtent) c).extent();
                return extent;
            }
    	}
        return null;
    }

    private ScalingMode getScalingMode() {
    	for (Command c : this.commands) {
            if (c instanceof ScalingMode) {
                return (ScalingMode)c;
            }
    	}
        return null;
    }

    public void showCGMCommands() {
    	showCGMCommands(System.out);
    }

	public void showCGMCommands(PrintStream stream) {
		for (Command c : this.commands) {
            stream.println("Command: " + c);
		}
	}

}

/*
 * vim:encoding=utf8
 */
