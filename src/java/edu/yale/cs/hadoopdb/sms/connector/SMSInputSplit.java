/**
 * Copyright 2009 HadoopDB Team (http://db.cs.yale.edu/hadoopdb/hadoopdb.html)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package edu.yale.cs.hadoopdb.sms.connector;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.io.Pathable;
import org.apache.hadoop.io.Text;

import edu.yale.cs.hadoopdb.connector.DBInputSplit;
import edu.yale.cs.hadoopdb.sms.connector.SMSInputSplit;

/**
 * This class extends the base DBInputSplit class and additionally
 * serializes path information needed by Hive to rebuild map operators
 * from the data path being processed.
 *
 */
public class SMSInputSplit extends DBInputSplit implements Pathable {

	public static final Log LOG = LogFactory.getLog(SMSInputSplit.class
			.getName());
	Path p;
	
	/**
	 * The path variable is set on calling getSplits in SMSInputFormat
	 * @param p path
	 */
	public void setPath(Path p) {
		this.p = p;
	}

	/**
	 * Returns the internal path variable, called by HiveInputSplit
	 * @return Path p
	 */
	public Path getPath() {
		return p;
	}

	/**
	 * readFields is used to deserialize information necessary to instantiate
	 * the SMSInputSplit at Map nodes. It first reads the path and 
	 * calls the DBInputSplit to deserialize remaining information.
	 */
	@Override
	public void readFields(DataInput in) throws IOException {		
		p = new Path(Text.readString(in));
		super.readFields(in);
	}

	/**
	 * write is used to serialize InputSplit information associated
	 * with splits created by SMSInputFormat getSplits. SMSInputSplit
	 * serializes Path information in addition to DBInputSplit information
	 */
	@Override
	public void write(DataOutput out) throws IOException {
		Text.writeString(out, p.toString());		
		super.write(out);
	}

}