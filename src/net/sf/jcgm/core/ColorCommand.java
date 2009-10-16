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

import java.awt.*;
import java.io.*;

/**
 * @author xphc (Philippe Cadé)
 * @author BBNT Solutions
 * @version $Id: ColorCommand.java,v 1.1 2009-03-17 13:07:46 xphc Exp $
 */
class ColorCommand extends Command {
    protected Color color = null;
	protected int colorIndex = -1;

    public ColorCommand(int ec, int eid, int l, DataInput in)
            throws IOException {
        super(ec, eid, l, in);
        
        if (ColorSelectionMode.getType().equals(ColorSelectionMode.Type.DIRECT)) {
        	this.color = makeDirectColor();
        }
        else if (ColorSelectionMode.getType().equals(ColorSelectionMode.Type.INDEXED)) {
        	this.colorIndex = makeColorIndex();
        }
    }
    
    @Override
	public String toString() {
    	StringBuilder sb = new StringBuilder();
    	if (this.color != null) {
    		sb.append(" directColor=").append(this.color);
    	}
    	else {
    		sb.append(" colorIndex=").append(this.colorIndex);
    	}
        return sb.toString();
    }
}

/*
 * vim:encoding=utf8
 */
