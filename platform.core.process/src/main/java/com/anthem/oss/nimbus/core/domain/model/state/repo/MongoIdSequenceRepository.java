package com.anthem.oss.nimbus.core.domain.model.state.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.anthem.oss.nimbus.core.SequenceException;
import com.anthem.oss.nimbus.core.entity.DBSequence;

/**
 * @author Rakesh Patel
 *
 */
@Repository
public class MongoIdSequenceRepository implements IdSequenceRepository {

	@Autowired
	MongoOperations mongoOperations;
	
	@Override
	public long getNextSequenceId(String key) throws SequenceException {
		//get sequence id
		Query query = new Query(Criteria.where("_id").is(key));
		
		//increase sequence id by 1
		Update update = new Update();
		update.inc("seq", 1);

		//return new increased id
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);

		//DBSequence seqId = mongoOperations.findAndModify(query, update, options, DBSequence.class);
		DBSequence seqId = mongoOperations.findAndModify(query, update, options, DBSequence.class, "sequence");

		//if no id, throws SequenceException]
		if (seqId == null) {
			throw new SequenceException("Unable to get sequence id for key : " + key);
		}

		return seqId.getSeq();
	}

}