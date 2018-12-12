package com.rubenexposito.flightsmap.presentation.flightlist

import android.graphics.Typeface
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.rubenexposito.flightsmap.R
import com.rubenexposito.flightsmap.domain.model.Airport
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_flight_list.*
import javax.inject.Inject

class FlightListActivity : AppCompatActivity(), FlightListContract.View {

    @Inject
    lateinit var presenter: FlightListContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_list)
        AndroidInjection.inject(this)
        presenter.onCreate()
        initView()
    }

    override fun onPrepared() {
        tvFrom.callOnClick()
    }

    override fun onPause() {
        presenter.onPause()
        super.onPause()
    }

    override fun showAirports(airportList: List<Airport>, from: Boolean) {
        with(rvItems.adapter as FlightListAdapter) {
            this.from = from
            addAirports(airportList)
            notifyDataSetChanged()
        }
    }

    override fun showError(@StringRes resId: Int) = Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()


    override fun updateAirportTo(text: String) = with(tvTo) {
        this.text = text
        setTextColor(getColor(R.color.black))
        typeface = Typeface.DEFAULT
    }

    override fun updateAirportFrom(text: String) {
        with(tvFrom) {
            this.text = text
            setTextColor(getColor(R.color.black))
            typeface = Typeface.DEFAULT
        }
        tvTo.callOnClick()
    }

    private fun initView() {
        tvFrom.setOnClickListener {
            tvFrom.setTextColor(getColor(R.color.colorPrimaryDark))
            tvFrom.typeface = Typeface.DEFAULT_BOLD
            presenter.requestAirports(true)
        }
        tvTo.setOnClickListener {
            tvTo.setTextColor(getColor(R.color.colorPrimaryDark))
            tvTo.typeface = Typeface.DEFAULT_BOLD
            presenter.requestAirports(false)
        }

        with(rvItems) {
            layoutManager = LinearLayoutManager(this@FlightListActivity)
            adapter = FlightListAdapter(presenter)
            setHasFixedSize(true)
        }
    }
}