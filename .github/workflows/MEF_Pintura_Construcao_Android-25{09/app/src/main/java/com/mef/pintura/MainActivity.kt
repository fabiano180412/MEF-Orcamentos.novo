package com.mef.pintura

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val yellow = Color.rgb(255, 208, 0)
    private val blue = Color.rgb(6, 42, 120)
    private val dark = Color.rgb(7, 16, 29)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showHome()
    }

    private fun baseScroll(): LinearLayout {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(dark)
            setPadding(16, 16, 16, 24)
        }
        return root
    }

    private fun title(text: String): TextView = TextView(this).apply {
        this.text = text
        textSize = 22f
        setTextColor(Color.WHITE)
        setTypeface(null, android.graphics.Typeface.BOLD)
        setPadding(0, 12, 0, 12)
    }

    private fun button(text: String, action: () -> Unit): Button = Button(this).apply {
        this.text = text
        setTextColor(Color.BLACK)
        setBackgroundColor(yellow)
        textSize = 15f
        setOnClickListener { action() }
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(0, 6, 0, 6) }
    }

    private fun showHome() {
        val root = baseScroll()
        val image = ImageView(this).apply {
            setImageResource(com.mef.pintura.R.drawable.mef_banner)
            adjustViewBounds = true
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
        root.addView(image, LinearLayout.LayoutParams(-1, -2))
        root.addView(title("M.E.F Pintura e Construção Civil em Geral"))

        root.addView(button("💰 ORÇAMENTO POR m²") { showBudget() })
        root.addView(button("🎨 SERVIÇOS") { showServices() })
        root.addView(button("📲 WHATSAPP") { openWhatsApp() })
        root.addView(button("📸 INSTAGRAM") { openUrl("https://instagram.com/mef.construcao.e.pintura.0026") })
        root.addView(button("📍 LOCALIZAÇÃO — Itapeva-MG") {
            openUrl("https://www.google.com/maps/search/?api=1&query=Itapeva-MG")
        })

        val info = TextView(this).apply {
            text = "Do início ao acabamento!\n(35) 9.9908-0356 — Marcelo\n(11) 9.8773-1981 — Fabiano"
            textSize = 16f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(0, 18, 0, 0)
        }
        root.addView(info)
        setContentView(ScrollView(this).apply { addView(root) })
    }

    private fun showServices() {
        val root = baseScroll()
        root.addView(title("Nossos serviços"))
        listOf(
            "Pintura residencial",
            "Pintura comercial",
            "Texturas e grafiatos",
            "Pequenos reparos",
            "Reformas em geral",
            "Acabamentos de qualidade",
            "Construção civil",
            "Serviço com compromisso"
        ).forEach { root.addView(button("• $it") {}) }
        root.addView(button("← Voltar") { showHome() })
        setContentView(ScrollView(this).apply { addView(root) })
    }

    private fun showBudget() {
        val root = baseScroll()
        root.addView(title("Orçamento por m²"))

        val service = Spinner(this).apply {
            adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item,
                listOf("Pintura", "Reboco", "Acabamento", "Reforma", "Outros serviços"))
        }
        root.addView(service)

        fun field(hint: String): EditText = EditText(this).apply {
            this.hint = hint
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            setTextColor(Color.WHITE)
            setHintTextColor(Color.LTGRAY)
            textSize = 17f
        }
        val area = field("Área em m² — ex.: 100")
        val price = field("Preço por m² — ex.: 25,00")
        root.addView(area)
        root.addView(price)

        val result = TextView(this).apply {
            textSize = 22f
            setTextColor(yellow)
            setPadding(0, 18, 0, 18)
        }
        root.addView(button("CALCULAR ORÇAMENTO") {
            val a = area.text.toString().replace(",", ".").toDoubleOrNull()
            val p = price.text.toString().replace(",", ".").toDoubleOrNull()
            if (a == null || a <= 0 || p == null || p < 0) {
                result.text = "Informe área e preço por m² válidos."
            } else {
                val total = a * p
                result.text = String.format(
                    Locale("pt", "BR"),
                    "%s\n%.2f m² × R$ %.2f/m²\nTotal: R$ %.2f",
                    service.selectedItem.toString(), a, p, total
                )
            }
        })
        root.addView(result)
        root.addView(button("ENVIAR ORÇAMENTO PELO WHATSAPP") { openWhatsApp() })
        root.addView(button("← Voltar") { showHome() })
        setContentView(ScrollView(this).apply { addView(root) })
    }

    private fun openWhatsApp() {
        val url = "https://wa.me/5511987731981?text=" +
                Uri.encode("Olá, M.E.F! Gostaria de solicitar um orçamento.")
        openUrl(url)
    }

    private fun openUrl(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}
