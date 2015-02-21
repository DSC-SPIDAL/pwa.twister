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

import cgl.imr.base.*;
import cgl.imr.base.impl.JobConf;
import cgl.imr.base.impl.ReducerConf;
import cgl.imr.types.*;

import java.io.*;
import java.util.List;

/**
 * @author Yang Ruan (yangruan at cs dot indiana dot edu)
 * @author Saliya Ekanayake (sekanaya at cs dot indiana dot edu)
 */
public class PWAReduceTask implements ReduceTask {

	private String outputPrefix;

	private int numOfSequences;
    private int numOfPartitions;
    private String dataDir;
    private String seqType;
    //private String idxFile;

	@Override
	public void close() throws TwisterException {
	}

	@Override
	public void configure(JobConf jobConf, ReducerConf mapConf)
			throws TwisterException {
        dataDir = jobConf.getProperty("dataDir");
		outputPrefix = jobConf.getProperty("outputPrefix");

		numOfSequences = Integer.parseInt(jobConf.getProperty("numOfSequences"));
        numOfPartitions = Integer.parseInt(jobConf.getProperty("numOfPartitions"));
        seqType = jobConf.getProperty("SeqType");
	}

	@Override
	public void reduce(ReduceOutputCollector collector, Key key,
                       List<Value> values) throws TwisterException {

		
        int rowBlockNumber = Integer.parseInt(((StringKey) key).getString());
        int rowSize = numOfSequences / numOfPartitions;
        int remainder;
        if ((remainder = numOfSequences % numOfPartitions) > 0 && rowBlockNumber < remainder) {
            rowSize++;
        }

        Block[] blocks = new Block[values.size()];
        Block b;
        for (Value value : values) {
            try {
                b = new Block(value.getBytes());
                System.out.println(b.getColumnBlockNumber());
                blocks[b.getColumnBlockNumber()] = b;
            } catch (SerializationException e) {
                e.printStackTrace();
            }
        }

//        String fnameScore
//        = dataDir + File.separator + outputPrefix + "score_" + String.valueOf(rowBlockNumber);
        String fnameLength
        = dataDir + File.separator + outputPrefix + "length_" + String.valueOf(rowBlockNumber);
        String fnameIdenticalPairs
        = dataDir + File.separator + outputPrefix + "identicalPairs_" + String.valueOf(rowBlockNumber);
        String fnamePID
        = dataDir + File.separator + outputPrefix + "pid_" + String.valueOf(rowBlockNumber);
        
//        String fnameScoreReverse
//        = dataDir + File.separator + outputPrefix + "scoreReverse_" + String.valueOf(rowBlockNumber);
        String fnameLengthReverse
        = dataDir + File.separator + outputPrefix + "lengthReverse_" + String.valueOf(rowBlockNumber);
        String fnameIdenticalPairsReverse
        = dataDir + File.separator + outputPrefix + "identicalPairsReverse_" + String.valueOf(rowBlockNumber);
        
//        String fnameScoreA
//        = dataDir + File.separator + outputPrefix + "scoreA_" + String.valueOf(rowBlockNumber);
//        String fnameScoreAReverse
//        = dataDir + File.separator + outputPrefix + "scoreAReverse_" + String.valueOf(rowBlockNumber);
//        String fnameScoreB
//        = dataDir + File.separator + outputPrefix + "scoreB_" + String.valueOf(rowBlockNumber);
//        String fnameScoreBReverse
//        = dataDir + File.separator + outputPrefix + "scoreBReverse_" + String.valueOf(rowBlockNumber);
        
        try {
//            DataOutputStream dosScore = new DataOutputStream(
//            		new BufferedOutputStream(new FileOutputStream(fnameScore)));
            DataOutputStream dosLength = new DataOutputStream(
            		new BufferedOutputStream(new FileOutputStream(fnameLength)));
            DataOutputStream dosIdenticalPairs = new DataOutputStream(
            		new BufferedOutputStream(new FileOutputStream(fnameIdenticalPairs)));
//            DataOutputStream dosScoreA = new DataOutputStream(
//            		new BufferedOutputStream(new FileOutputStream(fnameScoreA)));
//            DataOutputStream dosScoreB = new DataOutputStream(
//            		new BufferedOutputStream(new FileOutputStream(fnameScoreB)));
            DataOutputStream dosPID = new DataOutputStream(
            		new BufferedOutputStream(new FileOutputStream(fnamePID)));
            
//            DataOutputStream dosScoreReverse = null;
            DataOutputStream dosLengthReverse = null;
            DataOutputStream dosIdenticalPairsReverse = null;
//            DataOutputStream dosScoreAReverse = null;
//            DataOutputStream dosScoreBReverse = null;
            if(seqType.equals("DNA")){
//            	dosScoreReverse = new DataOutputStream(
//                		new BufferedOutputStream(new FileOutputStream(fnameScoreReverse)));
                dosLengthReverse = new DataOutputStream(
                		new BufferedOutputStream(new FileOutputStream(fnameLengthReverse)));
                dosIdenticalPairsReverse = new DataOutputStream(
                		new BufferedOutputStream(new FileOutputStream(fnameIdenticalPairsReverse)));
//                dosScoreAReverse = new DataOutputStream(
//                		new BufferedOutputStream(new FileOutputStream(fnameScoreAReverse)));
//                dosScoreBReverse = new DataOutputStream(
//                		new BufferedOutputStream(new FileOutputStream(fnameScoreBReverse)));
            }
            
//            short [][] score;
            short [][] length;
            short [][] identicalPairs;
//            short [][] scoreReverse;
            short [][] lengthReverse;
            short [][] identicalPairsReverse;
//            short [][] scoreA;
//            short [][] scoreAReverse;
//            short [][] scoreB;
//            short [][] scoreBReverse;
            //int count = 0;
            for (int i = 0; i < rowSize; i++) {
                for (Block block : blocks) {
                	//System.out.println("This count: " + count);
//                    score = block.getScore();
                    length = block.getLength();
                    identicalPairs = block.getIdenticalPairs();
//                    scoreReverse = block.getScoreReverse();
                    lengthReverse = block.getLengthReverse();
                    identicalPairsReverse = block.getIdenticalPairsReverse();
//                    scoreA = block.getScoreA();
//                    scoreAReverse = block.getScoreAReverse();
//                    scoreB = block.getScoreB();
//                    scoreBReverse = block.getScoreBReverse();

                    // Won't push "if" inside loop for the sake of performance
                    if (block.isTranspose()) {
                        for (int k = 0; k < block.getColSize(); k++) {
//                            dosScore.writeShort(score[k][i]);
                            dosLength.writeShort(length[k][i]);
                            dosIdenticalPairs.writeShort(identicalPairs[k][i]);
//                            dosScoreA.writeShort(scoreA[k][i]);
//                            dosScoreB.writeShort(scoreB[k][i]);
                            dosPID.writeShort((short) ((1 - identicalPairs[k][i] / (double) length[k][i]) * Short.MAX_VALUE));
                            if(seqType.equals("DNA")){
//                            	dosScoreReverse.writeShort(scoreReverse[k][i]);
                                dosLengthReverse.writeShort(lengthReverse[k][i]);
                                dosIdenticalPairsReverse.writeShort(identicalPairsReverse[k][i]);
//                                dosScoreAReverse.writeShort(scoreAReverse[k][i]);
//                                dosScoreBReverse.writeShort(scoreBReverse[k][i]);
                            }
                        }
                    } else {
                        for (int k = 0; k < block.getColSize(); k++) {
//                        	dosScore.writeShort(score[i][k]);
                        	dosLength.writeShort(length[i][k]);
                        	dosIdenticalPairs.writeShort(identicalPairs[i][k]);
//                        	dosScoreA.writeShort(scoreA[i][k]);
//                            dosScoreB.writeShort(scoreB[i][k]);
                            dosPID.writeShort((short) ((1 - identicalPairs[i][k] / (double) length[i][k]) * Short.MAX_VALUE));
                            
                        	if(seqType.equals("DNA")){
//                            	dosScoreReverse.writeShort(scoreReverse[i][k]);
                            	dosLengthReverse.writeShort(lengthReverse[i][k]);
                            	dosIdenticalPairsReverse.writeShort(identicalPairsReverse[i][k]);
//                            	dosScoreAReverse.writeShort(scoreAReverse[i][k]);
//                                dosScoreBReverse.writeShort(scoreBReverse[i][k]);
                        	}
                        }
                    }
                }
            }
//            dosScore.close();
            dosLength.close();
            dosIdenticalPairs.close();
//            dosScoreA.close();
//            dosScoreB.close();
            dosPID.close();
            if(seqType.equals("DNA")){
//            	dosScoreReverse.close();
                dosLengthReverse.close();
                dosIdenticalPairsReverse.close();
//                dosScoreAReverse.close();
//                dosScoreBReverse.close();
            }
            collector.collect(key, new StringValue(fnameLength));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
