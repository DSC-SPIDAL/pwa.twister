/*
 * Software License, Version 1.0
 *
 *  Copyright 2003 The Trustees of Indiana University.  All rights reserved.
 *
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1) All redistributions of source code must retain the above copyright notice,
 *  the list of authors in the original source code, this list of conditions and
 *  the disclaimer listed in this license;
 * 2) All redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the disclaimer listed in this license in
 *  the documentation and/or other materials provided with the distribution;
 * 3) Any documentation included with all redistributions must include the
 *  following acknowledgement:
 *
 * "This product includes software developed by the Community Grids Lab. For
 *  further information contact the Community Grids Lab at
 *  http://communitygrids.iu.edu/."
 *
 *  Alternatively, this acknowledgement may appear in the software itself, and
 *  wherever such third-party acknowledgments normally appear.
 *
 * 4) The name Indiana University or Community Grids Lab or SPIDAL,
 *  shall not be used to endorse or promote products derived from this software
 *  without prior written permission from Indiana University.  For written
 *  permission, please contact the Advanced Research and Technology Institute
 *  ("ARTI") at 351 West 10th Street, Indianapolis, Indiana 46202.
 * 5) Products derived from this software may not be called SPIDAL,
 *  nor may Indiana University or Community Grids Lab or SPIDAL appear
 *  in their name, without prior written permission of ARTI.
 *
 *
 *  Indiana University provides no reassurances that the source code provided
 *  does not infringe the patent or any other intellectual property rights of
 *  any other entity.  Indiana University disclaims any liability to any
 *  recipient for claims brought by any other entity based on infringement of
 *  intellectual property rights or otherwise.
 *
 * LICENSEE UNDERSTANDS THAT SOFTWARE IS PROVIDED "AS IS" FOR WHICH NO
 * WARRANTIES AS TO CAPABILITIES OR ACCURACY ARE MADE. INDIANA UNIVERSITY GIVES
 * NO WARRANTIES AND MAKES NO REPRESENTATION THAT SOFTWARE IS FREE OF
 * INFRINGEMENT OF THIRD PARTY PATENT, COPYRIGHT, OR OTHER PROPRIETARY RIGHTS.
 * INDIANA UNIVERSITY MAKES NO WARRANTIES THAT SOFTWARE IS FREE FROM "BUGS",
 * "VIRUSES", "TROJAN HORSES", "TRAP DOORS", "WORMS", OR OTHER HARMFUL CODE.
 * LICENSEE ASSUMES THE ENTIRE RISK AS TO THE PERFORMANCE OF SOFTWARE AND/OR
 * ASSOCIATED MATERIALS, AND TO THE PERFORMANCE AND VALIDITY OF INFORMATION
 * GENERATED USING SOFTWARE.
 */

package edu.indiana.soic.dsc.spidal.pwa.twister;

import cgl.imr.base.SerializationException;
import cgl.imr.base.Value;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a collection of computable blocks.
 * 
 * @author Yang Ruan -yangruan
 * @author Saliya Ekanayake (sekanaya at cs dot indiana dot edu)
 * 
 */
public class Region implements Value {
	private List<Block> blocks;

	public Region() {
		this.blocks = new ArrayList<Block>();
	}

	public Region(byte[] bytes) throws SerializationException {
		this();
		fromBytes(bytes);
	}

	public void addBlock(Block block) {
		this.blocks.add(block);
	}

	public List<Block> getBlocks() {
		return blocks;
	}

	@Override
	public void fromBytes(byte[] bytes) throws SerializationException {
		ByteArrayInputStream baInputStream = new ByteArrayInputStream(bytes);
		DataInputStream din = new DataInputStream(baInputStream);

		try {
			int count = din.readInt();
			if (count != 0) {
				int len;
				byte[] data;
				for (int i = 0; i < count; i++) {
					len = din.readInt();
					data = new byte[len];
					din.readFully(data);
					this.blocks.add(new Block(data));
				}
			}
			din.close();
			baInputStream.close();
		} catch (IOException ioe) {
			throw new SerializationException(ioe);
		}
	}

	@Override
	public byte[] getBytes() throws SerializationException {
		ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(baOutputStream);
		int count = this.blocks.size();

		byte[] marshalledBytes;
		try {
			dout.writeInt(count);
			byte[] data;
            for (Block block : this.blocks) {
                data = block.getBytes();
                dout.writeInt(data.length);
                dout.write(data);
            }
			dout.flush();
			marshalledBytes = baOutputStream.toByteArray();

			dout.close();
			baOutputStream.close();
		} catch (IOException ioe) {
			throw new SerializationException(ioe);
		}
		return marshalledBytes;
	}
}