package com.example.smartdailyexpensetracker.presentation.fragment
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartdailyexpensetracker.databinding.FragmentExpenseReportBinding
import com.example.smartdailyexpensetracker.presentation.adapter.CategoryTotalsAdapter
import com.example.smartdailyexpensetracker.presentation.adapter.DailyTotalsAdapter
import com.example.smartdailyexpensetracker.presentation.viewmodel.ExpenseReportViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.AndroidEntryPoint

import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class ExpenseReportFragment : Fragment() {

    private var _binding: FragmentExpenseReportBinding? = null
    private val binding get() = _binding!!

    private val vm: ExpenseReportViewModel by viewModels()

    private lateinit var dailyAdapter: DailyTotalsAdapter
    private lateinit var categoryAdapter: CategoryTotalsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentExpenseReportBinding.inflate(inflater, container, false)
        binding.vm = vm
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLists()
        observeData()
        setupChart()

        binding.btnExportCsv.setOnClickListener { exportCsv() }
        binding.btnExportPdf.setOnClickListener { exportPdf() }
        binding.btnShare.setOnClickListener { shareCsvOrPdf() }
    }

    private fun setupLists() {
        dailyAdapter = DailyTotalsAdapter(emptyList())
        binding.rvDailyTotals.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDailyTotals.adapter = dailyAdapter

        categoryAdapter = CategoryTotalsAdapter(emptyList())
        binding.rvCategoryTotals.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCategoryTotals.adapter = categoryAdapter
    }

    private fun observeData() {
        vm.dailyTotals.observe(viewLifecycleOwner) { list ->
            dailyAdapter.update(list)
            updateChart(list)
        }
        vm.categoryTotals.observe(viewLifecycleOwner) { list ->
            categoryAdapter.update(list)
        }
    }

    private fun setupChart() {
        val chart = binding.barChart
        chart.description.isEnabled = false
        chart.setFitBars(true)
        chart.axisRight.isEnabled = false
        chart.axisLeft.axisMinimum = 0f
        val x = chart.xAxis
        x.position = XAxis.XAxisPosition.BOTTOM
        x.granularity = 1f
        x.setDrawGridLines(false)
    }
    private fun updateChart(list: List<com.example.smartdailyexpensetracker.presentation.viewmodel.DailyTotal>) {
        val entries = list.mapIndexed { index, d ->
            BarEntry(index.toFloat(), d.amount.toFloat())
        }

        val set = BarDataSet(entries, "Daily spend").apply {
            valueTextSize = 10f
            color = resources.getColor(android.R.color.holo_blue_light, null)
            valueTextColor = resources.getColor(android.R.color.black, null)
        }

        val data = BarData(set).apply {
            barWidth = 0.20f // Narrower bars for cleaner look
        }

        binding.barChart.apply {
            this.data = data

            // Set x labels
            xAxis.apply {
                valueFormatter = com.github.mikephil.charting.formatter.IndexAxisValueFormatter(list.map { it.dateLabel })
                labelRotationAngle = -45f // Rotate labels to avoid overlap
                granularity = 1f
                setDrawGridLines(false)
                position = XAxis.XAxisPosition.BOTTOM
            }

            axisLeft.axisMinimum = 0f
            axisRight.isEnabled = false
            description.isEnabled = false
            legend.isEnabled = true

            setFitBars(true) // Adjusts x-axis range to fit bars nicely
            invalidate()
        }
    }

    // write CSV to cache and show Toast
    private fun exportCsv() {
        try {
            val csv = StringBuilder()
            csv.append("Date,Amount\n")
            vm.dailyTotals.value?.forEach {
                csv.append("${it.dateLabel},${it.amount}\n")
            }
            csv.append("\nCategory,Amount\n")
            vm.categoryTotals.value?.forEach {
                csv.append("${it.category},${it.amount}\n")
            }

            val file = File(requireContext().cacheDir, "expense_report.csv")
            FileOutputStream(file).use { it.write(csv.toString().toByteArray()) }

            Toast.makeText(requireContext(), "CSV saved: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Failed to export CSV: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    // create a simple PDF in cache
    private fun exportPdf() {
        try {
            val pdfFile = File(requireContext().cacheDir, "expense_report.pdf")
            val doc = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4-ish
            val page = doc.startPage(pageInfo)
            val canvas: Canvas = page.canvas
            val paint = Paint()
            paint.textSize = 14f
            var y = 30f
            paint.isFakeBoldText = true
            canvas.drawText("Expense Report - Last 7 days", 20f, y, paint)
            paint.isFakeBoldText = false
            paint.textSize = 12f
            y += 30f

            canvas.drawText("Daily totals:", 20f, y, paint); y += 20f
            vm.dailyTotals.value?.forEach {
                canvas.drawText("${it.dateLabel} - ₹${String.format("%.2f", it.amount)}", 30f, y, paint)
                y += 18f
            }

            y += 10f
            canvas.drawText("Category totals:", 20f, y, paint); y += 20f
            vm.categoryTotals.value?.forEach {
                canvas.drawText("${it.category} - ₹${String.format("%.2f", it.amount)}", 30f, y, paint)
                y += 18f
            }

            doc.finishPage(page)
            FileOutputStream(pdfFile).use { doc.writeTo(it) }
            doc.close()

            Toast.makeText(requireContext(), "PDF saved: ${pdfFile.absolutePath}", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Failed to create PDF: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    // share CSV if exists else PDF if exists else share text
    private fun shareCsvOrPdf() {
        try {
            val cache = requireContext().cacheDir
            val csv = File(cache, "expense_report.csv")
            val pdf = File(cache, "expense_report.pdf")

            val fileToShare = when {
                csv.exists() -> csv
                pdf.exists() -> pdf
                else -> null
            }

            if (fileToShare != null) {
                val uri: Uri = FileProvider.getUriForFile(requireContext(), requireContext().packageName + ".fileprovider", fileToShare)
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = if (fileToShare.name.endsWith(".csv")) "text/csv" else "application/pdf"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                startActivity(Intent.createChooser(intent, "Share report"))
            } else {
                // fallback: share text
                val txt = buildString {
                    append("Expense report (mock):\n\nDaily totals:\n")
                    vm.dailyTotals.value?.forEach { append("${it.dateLabel}: ₹${it.amount}\n") }
                    append("\nCategory totals:\n")
                    vm.categoryTotals.value?.forEach { append("${it.category}: ₹${it.amount}\n") }
                }
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, txt)
                }
                startActivity(Intent.createChooser(intent, "Share report"))
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Share failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
