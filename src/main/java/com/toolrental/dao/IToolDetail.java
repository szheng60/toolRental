package com.toolrental.dao;

import com.toolrental.model.ToolDetail;

/**
 * Created by xinyu on 11/2/2017.
 */
public interface IToolDetail {
    ToolDetail retrieveToolDetail(int toolId);
}
