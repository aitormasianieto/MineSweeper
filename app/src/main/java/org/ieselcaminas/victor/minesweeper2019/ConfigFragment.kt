package org.ieselcaminas.victor.minesweeper2019


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import org.ieselcaminas.victor.minesweeper2019.databinding.FragmentConfigBinding
import java.lang.NumberFormatException

/**
 * A simple [Fragment] subclass.
 */
class ConfigFragment : Fragment() {

    data class ConfigData(var numRows: Int, var numCols: Int)

    var configData = ConfigData(10,10)
    lateinit var binding: FragmentConfigBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_config, container, false)
        binding.buttonPlay.setOnClickListener() {
            //Navigation.findNavController(it).navigate(R.id.action_configFragment_to_gameFragment)
            it.findNavController().navigate(ConfigFragmentDirections.actionConfigFragmentToGameFragment(configData.numRows,configData.numCols))
        }
        binding.editTextCols.addTextChangedListener() {
            try {
                configData.numCols = it.toString().toInt()
            } catch (ex: NumberFormatException) {
                configData.numCols = 0
                //editTextRows.setText("")
            }
            binding.invalidateAll()
        }

        binding.editTextRows.addTextChangedListener() {
            try {
                configData.numRows = it.toString().toInt()
            } catch (ex: NumberFormatException) {
                configData.numRows = 0
                //editTextCols.setText("")
            }
            binding.invalidateAll()
        }

        setHasOptionsMenu(true)

        setSpinner()

        binding.config = this

        return binding.root
    }

    private fun setSpinner() {

        // Initializing a String Array
        val optionsStr = arrayOf(getString(R.string.easy),
                                getString(R.string.medium),
                                getString(R.string.difficult))

        // Initializing an ArrayAdapter
        val adapter = ArrayAdapter(context!!,
            android.R.layout.simple_spinner_item, optionsStr)

        // Set the drop down view resource
        adapter.setDropDownViewResource(
            android.R.layout.simple_dropdown_item_1line)

        // Finally, data bind the spinner object with dapter
        binding.spinner.adapter = adapter

        // Set an on item selected listener for spinner object
        binding.spinner.onItemSelectedListener =
                object: AdapterView.OnItemSelectedListener{

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(position) {
                    0 -> {
                        configData.numRows = 10
                        configData.numCols = 10
                        }
                    1 -> {
                        configData.numRows = 20
                        configData.numCols = 30
                        }
                    2 -> {
                        configData.numRows = 40
                        configData.numCols = 50
                        }
                }
                binding.invalidateAll()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            view!!.findNavController())

    }


}
