package com.toolrental.service;

import com.toolrental.model.ToolDetail;

/**
 * Created by xinyu on 11/5/2017.
 */
public interface IToolDetailService {
    ToolDetail retrieveToolDetail(int toolId);
}
