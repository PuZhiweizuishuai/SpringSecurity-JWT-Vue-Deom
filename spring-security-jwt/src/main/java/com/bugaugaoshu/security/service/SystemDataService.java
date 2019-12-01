package com.bugaugaoshu.security.service;

import com.bugaugaoshu.security.damain.CustomData;
import com.bugaugaoshu.security.damain.ResultDetails;
import com.bugaugaoshu.security.exception.CustomizeException;

import java.util.List;

/**
 * @author Pu Zhiwei {@literal puzhiweipuzhiwei@foxmail.com}
 * create          2019-12-01 16:22
 */
public interface SystemDataService {
    List<CustomData> get();

    CustomData select(String id) throws CustomizeException;

    ResultDetails delete(String id, String authorities) throws CustomizeException;

    CustomData create(CustomData customData);
}
