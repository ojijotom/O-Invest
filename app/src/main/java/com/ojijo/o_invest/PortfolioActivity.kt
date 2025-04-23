package com.ojijo.o_invest

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PortfolioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_portfolio) // Refers to activity_portfolio.xml

        // Find the ImageView and TextViews by their IDs
        val portfolioPhoto: ImageView = findViewById(R.id.portfolioPhoto) // ImageView ID in XML
        val portfolioName: TextView = findViewById(R.id.portfolioName) // TextView ID in XML
        val portfolioBio: TextView = findViewById(R.id.portfolioBio) // TextView for bio in XML

        // Set the text dynamically for portfolioName if needed (optional)
        portfolioName.text = "OJIJO TOM" // You can modify this text dynamically if required

        // Set the text dynamically for portfolioBio if needed (optional)
        portfolioBio.text = "I am the co-founder and developer of this app."

        // Set the image dynamically if needed
        // Example: portfolioPhoto.setImageResource(R.drawable.ojijo)

        // Alternatively, ensure that the ImageView's 'src' is correctly defined in XML:
        // android:src="@drawable/ojijo"  <- This should work if you set the image in the XML already.
    }
}
