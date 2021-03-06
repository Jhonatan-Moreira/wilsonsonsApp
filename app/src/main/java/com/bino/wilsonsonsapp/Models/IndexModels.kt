package com.bino.wilsonsonsapp.Models

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.media.MediaPlayer
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bino.wilsonsonsapp.Controllers.ControllersUniversais
import com.bino.wilsonsonsapp.Controllers.IndexControllers
import com.bino.wilsonsonsapp.R
import com.bino.wilsonsonsapp.Utils.CircleTransform
import com.bino.wilsonsonsapp.Utils.IntroQuestAdapter
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat


object IndexModels {

    val arrayPosicoesX: MutableList<Int> = ArrayList()
    val arrayPosicoesY: MutableList<Int> = ArrayList()

    var isverified: Boolean = false

    var posicaoUser = 0

    var userBd = "testeThiago"
    var userImg = "nao"

    var limitCertificate = "11/10/2023"

    var alertaEmbarcacao: String = "nao"
    var alertaDataEmbarque: String = "nao"

    val arrayCursos: MutableList<String> = ArrayList()
    val arrayCertificados: MutableList<String> = ArrayList()
    val arrayCertificadosValidade: MutableList<String> = ArrayList()

    val MAX_FASE_NUMBER = 4

    lateinit var objectQuestionsListRespondida: List<ObjectQuestions>
    lateinit var objectQuestions: ObjectQuestions

    lateinit var Sound: MediaPlayer;

    lateinit var btnPlayTheLevel: ImageView //vai ser invisivel. Será o mesmo formato da fase comum

    var pontos: Int = 0

    //var uId: String = "nao"

    fun placeBackGroundAsMap(
        backgroundPlaceHolder: ImageView,
        activity: Activity,
        fases: Int,
        layout: ConstraintLayout,
        playerAvatar: ImageView
    ) {

        placeMapOnScreen(activity, R.drawable.gamebackground1, backgroundPlaceHolder)

        val screenHeight = IndexControllers.calculateTheScreenSizeH(activity)
        var screenWidth =
            IndexControllers.calculateTheScreenSizeW(activity) - 100

        val intervalX =
            screenWidth / (fases / 2)  //pega a quantidade de fases e divide por 2. Digamos, se for 6 fases, vai dividir por 3. Assim teremos uma variação pequena
        var startPointX = 100 //para começar no cantinho

        val intervalY = (screenHeight) / fases
        var startPointY = screenHeight - 300

        val variation = 40

        var cont = 0
        while (cont < fases) {
            val imageView = ImageView(activity)
            // setting height and width of imageview
            imageView.layoutParams = LinearLayout.LayoutParams(80, 80)

            if (cont == 0) {
                imageView.x = startPointX.toFloat() //setting margin from left
                imageView.y = startPointY.toFloat() //setting margin from top
                arrayPosicoesX.add(startPointX)
                arrayPosicoesY.add(startPointY)
            } else if (cont + 1 == fases) {
                imageView.x = screenWidth.toFloat()//-100 //setting margin from left
                imageView.y = 0f //setting margin from top
                arrayPosicoesX.add(screenWidth)
                arrayPosicoesY.add(0)
            } else {

                if ((cont % 2) == 0) {
                    // par

                    startPointY = startPointY - intervalY
                    imageView.x = startPointX.toFloat() + 10
                    imageView.y = startPointY.toFloat()

                    arrayPosicoesX.add(startPointX + 10)
                    arrayPosicoesY.add(startPointY)
                } else {
                    // impar este é o segundo ponto

                    startPointY = startPointY - intervalY
                    imageView.x = startPointX.toFloat() + 300
                    imageView.y = startPointY.toFloat()
                    arrayPosicoesX.add(startPointX + 300)
                    arrayPosicoesY.add(startPointY)
                }

            }

            layout?.addView(imageView) //adding image to the layout

            Glide.with(activity).load(R.drawable.pontovermelho).into(imageView)
            cont++
        }

        placeThePlayerInitial(playerAvatar)

    }

    private fun placeMapOnScreen(activity: Activity, img: Int, imageView: ImageView) {

        Glide.with(activity).load(img).into(imageView)
    }

    private fun placeThePlayerInitial(playerAvatar: ImageView) {

        playerAvatar.x = arrayPosicoesX.get(posicaoUser).toFloat()
        playerAvatar.y = arrayPosicoesY.get(posicaoUser).toFloat() - 50

    }


    fun moveThePlayer(playerAvatar: ImageView, position: Int) {

        Log.d("teste", "posicaoUser é "+ posicaoUser)
        if (posicaoUser < arrayPosicoesX.size) {
            playerAvatar.animate().translationX(arrayPosicoesX.get(position).toFloat())
                .translationY(
                    arrayPosicoesY.get(position).toFloat()
                )
            placePlayButtonInSpot(btnPlayTheLevel)
        } else {
            finishTheLevel()
        }

    }

    private fun placePlayButtonInSpot(button: ImageView) {

        button.x = IndexModels.arrayPosicoesX.get(IndexModels.posicaoUser).toFloat()
        button.y = IndexModels.arrayPosicoesY.get(IndexModels.posicaoUser).toFloat()

    }

    fun setTheResultInMap(activity: Activity, layout: ConstraintLayout, acertou: Boolean) {

        posicaoUser++
        val imageView = ImageView(activity)
        // setting height and width of imageview
        imageView.layoutParams = LinearLayout.LayoutParams(120, 120)
        imageView.x = arrayPosicoesX.get(posicaoUser - 1).toFloat() //setting margin from left
        imageView.y = arrayPosicoesY.get(posicaoUser - 1).toFloat() //setting margin from top
        layout?.addView(imageView) //adding image to the layout

        val textView = TextView(activity)
        textView.layoutParams = LinearLayout.LayoutParams(80, 40)
        textView.x = arrayPosicoesX.get(posicaoUser - 1).toFloat()
        textView.setTextColor(Color.YELLOW)
        textView.y = arrayPosicoesY.get(posicaoUser - 1).toFloat() - 100
        layout?.addView(textView)

        if (acertou) {
            textView.setText((pontos + 100).toString())
            Glide.with(activity).load(R.drawable.acertonomapa).into(imageView)
        } else {

            textView.setText((pontos - 50).toString())
            Glide.with(activity).load(R.drawable.erronomapa).into(imageView)
        }

    }

    fun stopSoundIntro() {
        if (Sound.isPlaying()) {
            Sound.stop();
        }
    }

    fun openIntroQuest(
        layIntroQuest: ConstraintLayout,
        recyclerView: RecyclerView,
        activity: Activity,
        objectQuestions: ObjectQuestions
    ) {

        //layInicial.visibility = View.GONE
        layIntroQuest.visibility = View.VISIBLE

        var objectIntro: List<ObjectIntro> =
            ConsultsQuestionsModel.selectIntro(objectQuestions.id_intro)

        when (objectQuestions.id_intro) {
            1 -> Sound = MediaPlayer.create(activity, R.raw.intro1)
            2 -> Sound = MediaPlayer.create(activity, R.raw.intro2)
            else -> Sound = MediaPlayer.create(activity, R.raw.intro1)
        }
        Sound.start()

        /*   val arrayTitulo: MutableList<String> = ArrayList()
           arrayTitulo.add("Olaaaa")
           arrayTitulo.add("Este é o titulo 2")
           val arrayTexto: MutableList<String> = ArrayList()
           arrayTexto.add("Este é o texto 1")
           arrayTexto.add("Este é o texto 2, um texto maior para podermos testar")
           val arrayImage: MutableList<String> = ArrayList()
           arrayImage.add("https://firebasestorage.googleapis.com/v0/b/wilsonsonshack.appspot.com/o/problemas%2Fquadrinho.jpg?alt=media&token=c27bd083-fe07-4a37-8557-14125a99ebf4")
           arrayImage.add("https://firebasestorage.googleapis.com/v0/b/wilsonsonshack.appspot.com/o/problemas%2Fquadrinho2.jpg?alt=media&token=843e406c-cca5-4fa8-9ad5-5a9f1adce458")
   */
        val adapter: IntroQuestAdapter = IntroQuestAdapter(activity, objectIntro)

        //chame a recyclerview
        //val recyclerView: RecyclerView = findViewById(R.id.question_intro_recyclerView)

        //define o tipo de layout (linerr, grid)
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(activity)

        //coloca o adapter na recycleview
        recyclerView.adapter = adapter

        recyclerView.layoutManager = linearLayoutManager

        // Notify the adapter for data change.
        adapter.notifyDataSetChanged()

        recyclerView.addOnItemTouchListener(
            IndexModels.RecyclerTouchListener(
                activity,
                recyclerView!!,
                object : IndexModels.ClickListener {

                    override fun onClick(view: View, position: Int) {
                        Log.d("teste", objectIntro.get(position).title)
                        //Toast.makeText(this@MainActivity, !! aNome.get(position).toString(), Toast.LENGTH_SHORT).show()
                    }

                    override fun onLongClick(view: View?, position: Int) {

                    }
                })
        )


    }

    fun checkUserCheckpoint(userAvatar: ImageView) {
        objectQuestionsListRespondida = ConsultsQuestionsModel.selectQuestionsRespondidas()
        objectQuestions = ObjectQuestions()

        Log.d(
            "teste",
            "o tamanho de objsectlistRespondidas é " + objectQuestionsListRespondida.size
        )

        if (objectQuestionsListRespondida.size < IndexModels.MAX_FASE_NUMBER && objectQuestionsListRespondida.size != 0) {

            objectQuestions = ConsultsQuestionsModel.selectQuestionPerId(
                objectQuestionsListRespondida.get(objectQuestionsListRespondida.size - 1).id
            )

        } else {
            objectQuestions = ConsultsQuestionsModel.selectQuestionPerId(0)
        }

        //verifica a ultima fase do user
        IndexModels.posicaoUser = objectQuestions.id

        //coloca ele na fase
        if (IndexModels.posicaoUser != 0) {
            IndexModels.moveThePlayer(userAvatar, posicaoUser)
        }
    }

    fun placeImage(imageView: ImageView, activity: Activity) {

        if (userImg.equals("nao")) {
            try {
                Glide.with(activity)
                    .load(R.drawable.avatar)
                    .thumbnail(0.2f)
                    .skipMemoryCache(true)
                    .transform(CircleTransform(activity))
                    .into(imageView)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            try {
                Glide.with(activity)
                    .load(R.drawable.avatar)
                    .thumbnail(0.2f)
                    .skipMemoryCache(true)
                    .transform(CircleTransform(activity))
                    .into(imageView)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    private fun finishTheLevel() {

        //calcular aqui como o user foi
        //exibir um resumo
        //avisar se tem mais ou se acabou

    }

    fun checkCertificate(): Boolean {

        val today = ControllersUniversais.getDate()
        //val limiteDate = GetfutureDate(30)

        val format = SimpleDateFormat("dd/MM/yyyy")
        val date1 = format.parse(limitCertificate)
        val date2 = format.parse(today)

        if (date1.compareTo(date2) >= 0) {  //se for hoje ou no futuro
            return false //menor nao precisa avisar
        } else {
            return true  //maior  precisa
        }
    }

    interface ClickListener {
        fun onClick(view: View, position: Int)

        fun onLongClick(view: View?, position: Int)
    }

    fun loadCourses(): Boolean {

        arrayCursos.clear()

        arrayCursos.add("Rebocador Torda")
        arrayCursos.add("Rebocador Skua")
        arrayCursos.add("Rebocador Biguá")
        arrayCursos.add("Rebocador Petrel")
        arrayCursos.add("Rebocador Saveiro")
        arrayCursos.add("Rebocador Saveiro Atobá")
        arrayCursos.add("Rebocador Talha-mar")
        arrayCursos.add("Rebocador Saveiro Pelicano")
        arrayCursos.add("Rebocador Saveiro Albatroz")
        arrayCursos.add("Rebocador Cormoran")
        arrayCursos.add("Rebocador Batuíra")
        arrayCursos.add("Rebocador Sterna")
        arrayCursos.add("Rebocador Prion")
        arrayCursos.add("Rebocador Tagaz")
        arrayCursos.add("Rebocador Zarapito")
        arrayCursos.add("Rebocador Alcatraz")
        arrayCursos.add("Rebocador Larus")
        arrayCursos.add("Rebocador Pinguim")

        return true

    }

    internal class RecyclerTouchListener(
        context: Context,
        recyclerView: RecyclerView,
        private val clickListener: ClickListener?
    ) : RecyclerView.OnItemTouchListener {

        private val gestureDetector: GestureDetector

        init {
            gestureDetector =
                GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onSingleTapUp(e: MotionEvent): Boolean {
                        return true
                    }

                    override fun onLongPress(e: MotionEvent) {
                        val child = recyclerView.findChildViewUnder(e.x, e.y)
                        if (child != null && clickListener != null) {
                            clickListener.onLongClick(child, recyclerView.getChildPosition(child))
                        }
                    }
                })
        }

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {

            val child = rv.findChildViewUnder(e.x, e.y)
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child))
            }
            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

        }
    }

    fun checkCadInfo(): Boolean {

        val objectUser: ObjectUser = ObjectUser()

        if (objectUser.state == 0 || objectUser.number == null || objectUser.name == null || objectUser.datenascimento == null || objectUser.datenascimento == null || objectUser.cargo == 0 || objectUser.photo == null) {
            //openPopUpCadInfo("Completar", "Fazer depois", findViewById(R.id.layoutPrincipal))
            return false
        } else {
            return true
        }
    }

    fun loadArrayStates (position: Int): String{

        var list_of_items = arrayOf(
            "Selecione Estado",
            "AC",
            "AL",
            "AP",
            "AM",
            "BA",
            "CE",
            "DF",
            "ES",
            "GO",
            "MA",
            "MT",
            "MS",
            "MG",
            "PA",
            "PB",
            "PR",
            "PE",
            "PI",
            "RJ",
            "RN",
            "RS",
            "RO",
            "RR",
            "SC",
            "SP",
            "SE",
            "TO")

        return list_of_items[position]

    }

}