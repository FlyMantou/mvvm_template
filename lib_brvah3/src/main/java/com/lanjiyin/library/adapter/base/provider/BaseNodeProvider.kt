package com.lanjiyin.library.adapter.base.provider

import com.lanjiyin.library.adapter.base.BaseNodeAdapter
import com.lanjiyin.library.adapter.base.entity.node.BaseNode

abstract class BaseNodeProvider : BaseItemProvider<BaseNode>() {

    override fun getAdapter(): BaseNodeAdapter? {
        return super.getAdapter() as? BaseNodeAdapter
    }
}