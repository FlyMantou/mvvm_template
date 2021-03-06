package com.viet.mine.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.chenenyu.router.annotation.Route
import com.viet.mine.R
import com.viet.mine.adapter.CardRvAdapter
import com.viet.mine.adapter.CustomSnapHelper
import com.viet.mine.viewmodel.MineWalletViewModel
import com.viet.news.core.config.Config
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.ui.InjectActivity
import com.viet.news.core.ui.TabFragmentAdapter
import kotlinx.android.synthetic.main.activity_mine_wallet.*
import javax.inject.Inject

/**
 * @Description 我的钱包
 * @Author null
 * @Email zongjia.long@merculet.io
 * @Version
 */
@Route(value = [Config.ROUTER_MINE_WALLET_ACTIVITY],interceptors = [(Config.LOGIN_INTERCEPTOR)])
class MineWalletActivity : InjectActivity() {

    @Inject
    internal lateinit var tabAdapter: TabFragmentAdapter
    lateinit var mMySnapHelper: CustomSnapHelper
    lateinit var adapter: CardRvAdapter

    private val model by viewModelDelegate(MineWalletViewModel::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine_wallet)
        initView()
        initData()
    }

    private fun initView() {
        tabAdapter.setFragment(model.fragments)
        vp_content.adapter = tabAdapter

        val list = ArrayList<Entity>()
        list.add(Entity("总资产(MB钻)", "10,000.00"))
        list.add(Entity("POWER", "25,000.00"))
        adapter = CardRvAdapter(this, list)
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv.adapter = adapter
        mMySnapHelper = CustomSnapHelper()
        mMySnapHelper.attachToRecyclerView(rv)
        initListener()
//        adapter =  ItemAdapter(this,list)
//        vp_card.offscreenPageLimit = 3
//        vp_card.adapter = adapter
//        vp_card.setPageTransformer(false, ScaleTransformer())
    }

    private fun initListener() {
        mMySnapHelper.setItemChangedDelegate {
            onItemChanged = {
                setPageStatus(it)
            }
        }

        adapter.setClickDelegate {
            onItemClick = {
                setPageStatus(it)
            }
        }
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val childCount = rv.childCount
                val location = IntArray(2)
                for (i in 0 until childCount) {
                    val v = rv.getChildAt(i)
                    v.getLocationOnScreen(location)
                    val recyclerViewCenterX = rv.left + rv.width / 2
                    val itemCenterX = location[0] + v.width / 2

                    // 两边的图片缩放比例
                    val scale = 0.85f
                    //某个item中心X坐标距recyclerview中心X坐标的偏移量
                    val offX = Math.abs(itemCenterX - recyclerViewCenterX)
                    //在一个item的宽度范围内，item从1缩放至scale，那么改变了（1-scale），从下列公式算出随着offX变化，item的变化缩放百分比

                    val percent = offX * (1 - scale) / v.width
                    //取反哟
                    var interpretateScale = 1 - percent


                    //这个if不走的话，得到的是多级渐变模式
                    if (interpretateScale < scale) {
                        interpretateScale = scale
                    }
                    v.scaleX = interpretateScale
                    v.scaleY = interpretateScale

                }
            }
        })
        vp_content.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                setPageStatus(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
    }

    private fun initData() {

    }

    data class Entity(val name: String, val count: String)

    /**
     * 设置联动效果
     */
    private fun setPageStatus(position:Int){
        when (position) {
            0 -> {
                rv.smoothScrollToPosition(0)
                vp_content.currentItem = 0
            }
            1 -> {
                rv.smoothScrollToPosition(1)
                vp_content.currentItem = 1
            }
        }
    }
}