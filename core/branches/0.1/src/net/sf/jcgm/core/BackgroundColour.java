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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.io.*;


/**
 * Class=2, Element=7
 * @author xphc (Philippe Cadé)
 * @author BBNT Solutions
 * @version $Id$
 */
class BackgroundColour extends Command {
    private Color backgroundColor;

	public BackgroundColour(int ec, int eid, int l, DataInput in)
            throws IOException {
        super(ec, eid, l, in);
        
        this.backgroundColor = makeDirectColor();
        
        // make sure all the arguments were read
        assert (this.currentArg == this.args.length);
    }

	@Override
	public void paint(CGMDisplay d) {
		Graphics g = d.getGraphics2D();
		g.setColor(this.backgroundColor);

		Point2D.Double[] extent = d.getExtent();
		int botLeftX = (int)(extent[0].x);
		int botLeftY = (int)(extent[0].y);
		int topRightX = (int)(extent[1].x);
		int topRightY = (int)(extent[1].y);
		g.fillRect(botLeftX, topRightY, topRightX - botLeftX, botLeftY - topRightY);
	}

	@Override
	public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("BackgroundColour ").append(this.backgroundColor);
    	return sb.toString();
    }
}

/*
 * vim:encoding=utf8
 */
