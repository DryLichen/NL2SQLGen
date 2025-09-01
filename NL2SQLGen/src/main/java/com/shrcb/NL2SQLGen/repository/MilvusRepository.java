package com.shrcb.NL2SQLGen.repository;

import com.shrcb.NL2SQLGen.model.entity.VectorRecord;

import java.util.List;

public interface MilvusRepository {
    void insert(VectorRecord record);
    List<VectorRecord> search(float[] vector, int topK);
}

