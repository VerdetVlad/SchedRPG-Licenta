package com.vladv.questsched.utilities
import com.example.schedrpg.R
import java.util.Vector

class AvatarList() {

    companion object {
        @JvmStatic var avatarList: ArrayList<Avatar> = arrayListOf(
            Avatar(R.drawable.avatar_face1,R.drawable.avatar_full1,"Warrior(m)"),
            Avatar(R.drawable.avatar_face2,R.drawable.avatar_full2,"Warrior(f)"),
            Avatar(R.drawable.avatar_face3,R.drawable.avatar_full3,"Thief(m)"),
            Avatar(R.drawable.avatar_face4,R.drawable.avatar_full4,"Thief(f)"),
            Avatar(R.drawable.avatar_face5,R.drawable.avatar_full5,"Beast Master(m)"),
            Avatar(R.drawable.avatar_face6,R.drawable.avatar_full6,"Beast Master(f)"),
            Avatar(R.drawable.avatar_face7,R.drawable.avatar_full7,"Mage(m)"),
            Avatar(R.drawable.avatar_face8,R.drawable.avatar_full8,"Mage(f)"))

    }

}