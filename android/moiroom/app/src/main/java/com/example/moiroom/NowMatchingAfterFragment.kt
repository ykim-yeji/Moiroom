package com.example.moiroom

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.moiroom.adapter.CardAdapter
import com.example.moiroom.databinding.FragmentNowMatchingAfterBinding
import com.example.moiroom.data.MatchedMemberList
import com.example.moiroom.data.Member
import com.example.moiroom.utils.cacheMatchedMemberList
import com.example.moiroom.utils.cacheUserInfo

class NowMatchingAfterFragment : Fragment() {
    private lateinit var binding: FragmentNowMatchingAfterBinding
    private var toggled: Boolean = true

    val cachedUserInfo: Member? by lazy { cacheUserInfo.get("userInfo") }
    val cachedMatchedMemberList: MatchedMemberList? by lazy { cacheMatchedMemberList.get("matchedMemberList") }

//    private val cardInfoList = TestData.cardInfoList

    // 프래그먼트 뷰 생성 : XML 레이아웃을 이용하여 프래그먼트 뷰 생성
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNowMatchingAfterBinding.inflate(inflater, container, false)
        return binding.root
    }

    // 뷰 생성 이후 동작
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView의 레이아웃 매니저를 설정
        val gridLayoutManager = GridLayoutManager(context, 1)
        binding.recyclerView.layoutManager = gridLayoutManager
        binding.viewPager2.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        // 체크된 상태에 따른 초기 화면 설정
        binding.viewPager2.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        setCardAdapter(true)

        // 현재 몇번째 뷰페이저를 보고 있는지 확인
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                // 새 페이지가 선택되었을 때 호출됩니다.
                Log.d("ViewPager", "Current Page: $position")
                binding.currentCard.text = "${position + 1}"

                if (position == 0) {
                    binding.pagerLeft.visibility = View.GONE
                } else if (position + 1 == cachedMatchedMemberList?.totalElememts) {
                    binding.pagerRight.visibility = View.GONE
                } else {
                    binding.pagerLeft.visibility = View.VISIBLE
                    binding.pagerRight.visibility = View.VISIBLE
                }
            }
        })
        if (cachedMatchedMemberList != null) {
            binding.totalCard.text = "${cachedMatchedMemberList?.totalElememts}"
        }

        binding.layoutChanger.setOnClickListener {
            toggled = !toggled
            if (toggled) {
                setToViewPager()
            } else {
                setToRecyclerView()
            }
            setCardAdapter(toggled)
        }

        binding.reMatchButton.setOnClickListener {
            val intent = Intent(context, LoadingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setToViewPager() {
        val iconListDrawable: Drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_chat_several)!!

        binding.viewPager2.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.cardIndicator.visibility = View.VISIBLE

        binding.layoutChangerIcon.setImageDrawable(iconListDrawable)
    }

    private fun setToRecyclerView() {
        val iconDrawable: Drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_chat_one)!!

        binding.viewPager2.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.cardIndicator.visibility = View.GONE

        binding.pagerLeft.visibility = View.GONE
        binding.pagerRight.visibility = View.GONE

        binding.layoutChangerIcon.setImageDrawable(iconDrawable)
    }

    private fun setCardAdapter(isButton1Checked: Boolean) {
        if (cachedMatchedMemberList != null && cachedUserInfo != null) {
            val cardAdapter = CardAdapter(requireContext(), cachedMatchedMemberList!!.content, cachedUserInfo!!, isButton1Checked)

            if (isButton1Checked) {
                binding.viewPager2.adapter = cardAdapter
                binding.recyclerView.adapter = null
            } else {
                binding.viewPager2.adapter = null
                binding.recyclerView.adapter = cardAdapter

                cardAdapter.setOnItemClickListener { position ->
                    Log.d("MYTAG", "setCardAdapter: $position")
                    val cardAdapter2 = CardAdapter(requireContext(), cachedMatchedMemberList!!.content, cachedUserInfo!!, !isButton1Checked)
                    setToViewPager()

                    binding.viewPager2.adapter = cardAdapter2
                    binding.recyclerView.adapter = null
                    toggled = !toggled
                    binding.viewPager2.currentItem = position

                }
            }
        }
    }

//    private fun showDetailFragment(cardInfo: CardInfo) {
//        val detailFragment = NewCardDetailDialogFragment.newInstance(cardInfo)
//        val oldFragment = parentFragmentManager.findFragmentByTag("cardDetail")
//        oldFragment?.let {
//            parentFragmentManager.beginTransaction().remove(it).commit()
//        }
//        detailFragment.show(parentFragmentManager, "cardDetail")
//        binding.recyclerView.visibility = View.GONE
//        binding.viewPager2.visibility = View.GONE
//    }
}
