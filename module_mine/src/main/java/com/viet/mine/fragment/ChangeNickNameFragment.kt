package com.viet.mine.fragment

import android.annotation.SuppressLint
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.chenenyu.router.annotation.Route
import com.jakewharton.rxbinding2.widget.RxTextView
import com.viet.mine.R
import com.viet.mine.viewmodel.AccountInfoViewModel
import com.viet.news.core.config.Config
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.domain.RefreshUserInfoEvent
import com.viet.news.core.domain.Settings
import com.viet.news.core.domain.User
import com.viet.news.core.ext.clickWithTrigger
import com.viet.news.core.ext.finishWithAnim
import com.viet.news.core.ui.BaseActivity
import com.viet.news.core.ui.BaseFragment
import com.viet.news.core.utils.RxBus
import kotlinx.android.synthetic.main.fragment_mine_setting_nickname.*

/**
 * @Description 修改昵称
 * @Author null
 * @Email zongjia.long@merculet.io
 * @Version
 */
@Route(value = [Config.ROUTER_MINE_EDIT_CHANGE_NICKNAME_FRAGMENT])
class ChangeNickNameFragment : BaseFragment() {
    private val model by viewModelDelegate(AccountInfoViewModel::class, true)

    override fun isSupportSwipeBack(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_mine_setting_nickname
    }

    override fun initView(view: View) {
        initListener(view)
    }

    @SuppressLint("CheckResult")
    private fun initListener(view: View) {
        RxTextView.textChanges(nickname_input)
                .subscribe {
                    model.nickName.value = it.toString()
                    model.checkNickNameSubmitBtnEnable()
                }

        confirm_btn.clickWithTrigger {
            if (model.nickNameSubmitEnable()) {
                model.updateNickName(this, nickname_input.text.toString()) { isOk ->
                    if (isOk) {
                        Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show()
                        User.currentUser.userName = nickname_input.text.toString()
                        Settings.create(context!!).userName = nickname_input.text.toString()
                        RxBus.get().post(RefreshUserInfoEvent())
                    } else {
                        Toast.makeText(context, "修改失败", Toast.LENGTH_SHORT).show()
                    }
                    (activity as BaseActivity).finishWithAnim()
                }
            }
        }
        //注册按钮能否点击更新
        model.submitEnable.observe(this, Observer {
            confirm_btn.isEnabled = it != null && it
        })
    }


}