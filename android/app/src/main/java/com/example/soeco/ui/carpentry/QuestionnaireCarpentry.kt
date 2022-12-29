package com.example.soeco.ui.carpentry

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.soeco.R
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

class QuestionnaireCarpentry : Fragment() {

    val args: QuestionnaireCarpentryArgs by navArgs()
    private val navigation: NavController by lazy { findNavController() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_questionnaire, container, false)

        val orderNumberTextView : TextView = view.findViewById(R.id.textView_questionnaireOrderNumber)
        val sendDeviationButton : Button = view.findViewById(R.id.button_reportDeviation)

        orderNumberTextView.text = args.orderNumber

        // takes back to dashboard
        sendDeviationButton.setOnClickListener {
            val action = QuestionnaireCarpentryDirections.actionQuestionnaireCarpentryToDashBoardFragment()
            navigation.navigate(action)
        }

        return view
    }


}