package com.example.myapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.text.isDigitsOnly
import net.objecthunter.exp4j.ExpressionBuilder
import kotlinx.android.synthetic.main.activity_main.*
import net.objecthunter.exp4j.Expression
import java.util.ArrayDeque

var map = mapOf('(' to 0,'+' to 1, '-' to 1, '*' to 2, '/' to 2)


fun toPostfix(exp: String): ArrayDeque<String>
{
    var str = ""
    var stack = ArrayDeque<Char>()
    var words = ArrayDeque<String>()
    var old : Char = '0';

    for (i in exp)
    {
        if(i.isDigit() || i == '.')
            str+=i
        else if (i == '(')
        {
            stack.addLast(i)
            if(str.isNotEmpty())
                words.push(str)
            str ="0"
        }

        else if (i == ')')
        {
            if (str.isNotEmpty())
                words.addLast(str)
            str=""
            while(stack.size > 0 && stack.last != '(')
            {
                words.addLast(stack.removeLast().toString())
                println(words.last)
            }
            stack.removeLast()
        }
        else
        {
            if(str.isNotEmpty())
                words.addLast(str)
            str=""

            while ((stack.size > 0) && (map[stack.last!!]!!>=map[i]!!))
                words.addLast(stack.removeLast().toString())


            stack.addLast(i)
        }
        old = i
    }
    if(str.isNotEmpty())
        words.addLast(str)
    while(!stack.isEmpty())
        words.addLast(stack.removeLast().toString())
    Log.d("about", "abob")
    println(words)
    println("----------------------------------------------------------")
    return words
}
var Inf = 9999999999.0
var error0 = false
fun doing(f: Double, s:Double, oper:String): Double
{
    if(oper == "+")
        return f+s
    if(oper == "-")
        return f-s
    if(oper == "*")
        return f*s
    if(oper == "/")
    {
        if(s==0.0)
        {
            error0 = true
            return Inf
        }
        return f/s
    }
    return 0.0
}

fun isDigits(str : String) : Boolean
{
    for(i in str)
        if (!i.isDigit() && i!='.')
            return false
    return true
}

fun calc(e: String) : Double
{
    var exp = e
    error0 = false
    if(exp[0]!='0')
        exp = "0$exp"
    var postfix : ArrayDeque<String> = toPostfix(exp)
    var vars = ArrayDeque<Double>()

    for(i in postfix)
    {
        if(isDigits(i))
        {
            vars.addLast(i.toDouble())
        }
        else
        {
            var s = vars.removeLast()
            var f = vars.removeLast()
            vars.addLast(doing(f,s,i))
        }
    }
    return vars.last
}

class MainActivity : AppCompatActivity() {
    private var i:Int = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //println(calc("-12+(4*2*1+10)/2-15"))
        Log.d("about", "onCreate")
    }

    public fun onBtnClick(view: View)
    {
        val text = findViewById<TextView>(R.id.textData)
        var oldText: String = text.getText() as String
        when (view.id) {//do a switch on the id instead of the entire object
            R.id.btn0 ->
                add("0")
            R.id.btn1 ->
                add("1")
            R.id.btn2 ->
                add("2")
            R.id.btn3 ->
                add("3")
            R.id.btn4 ->
                add("4")
            R.id.btn5 ->
                add("5")
            R.id.btn6 ->
                add("6")
            R.id.btn7 ->
                add("7")
            R.id.btn8 ->
                add("8")
            R.id.btn9 ->
                add("9")
            R.id.btnPlus ->
                add("+")
            R.id.btnMinus ->
                add("-")
            R.id.btnMultiply ->
                add("*")
            R.id.btnDiv ->
                add("/")
            R.id.btnPoint ->
                add(".")
            R.id.btnOpenBracket ->
                add("(")
            R.id.btnCloseBracket ->
                add(")")
            R.id.btnDel -> {
                if (oldText.length != 0)
                {
                    text.setText(oldText.substring(0, oldText.length - 1))
                    textRes.text = ""
                }
            }
            R.id.btnRes ->{
                try {
                    val res = calc(text.text.toString())
                    if(error0 == true) {
                        textRes.text = "Error"
                        return
                    }
                    val longRes = res.toLong()
                    if (res == longRes.toDouble())
                        textRes.text = longRes.toString()
                    else
                        textRes.text = res.toString()
                }
                catch (ex : Exception)
                {
                    textRes.text = "Error"
                }

            }
            R.id.btnClear->
            {
                textData.text = ""
                textRes.text = ""
            }


        }
    }
    public fun add(newItem: String)
    {
        if(textRes.text == "Error")
        {
            textData.text = textData.text.toString()+ newItem
            textRes.text = ""
        }
        else if (newItem == "+" || newItem == "-" || newItem == "*" || newItem == "/") {

            if(textData.text.length == 0 && newItem != "-")
            {
                textData.text = ""
            }
            else if(textData.text.length == 0 && newItem == "-")
            {
                textData.text = "-"
            }
            else if(textData.text[textData.text.length-1]=='+' ||textData.text[textData.text.length-1]=='-' || textData.text[textData.text.length-1]=='/'||textData.text[textData.text.length-1]=='*' )
            {
                println(textData.text[textData.text.length-1])
                textData.setText(textData.text.substring(0, textData.text.length - 1)  + newItem)
            }
            else if (textRes.text.isNotEmpty()){
                textData.text = textRes.text.toString()+ newItem
                textRes.text = ""
            }
            else
            {
                textData.text =  textData.text.toString()  + newItem
            }

        }
        else
        {
            textData.text = textData.text.toString() + newItem
            textRes.text = ""
        }

    }
}
