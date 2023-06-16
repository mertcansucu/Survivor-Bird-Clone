package com.mertcansucu.survivorbird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class SurvivorBird extends ApplicationAdapter {
	SpriteBatch batch;//arka plan görsellerini oluşturup eklemek için oluşturdum
	Texture background;
	Texture bird;
	Texture bee1;
	Texture bee2;
	Texture bee3;

	float birdX = 0;//x ve y değerlerini değişkene attım
	float birdY = 0;//çünkü kuş uçtuğunda yeri değişecek,bizde rahatlıkla değiştirelim diye

	int gameState = 0;//kullanıcı oyuna başlamağı zamanki değer

	//buranın amacı kuş aşağı doğru hareket etmesini istiyorum hangi hızda aşağı ineceğini ayarladım
	float velocity = 0;//hız değişkeni
	float gravity = 0.7f;//yer çekimi

	float enemyVelocity = 7;//arının hızı

	Random random;

	int score = 0;//score tutmak için
	int scoreEnemy = 0;

	BitmapFont font;//score ekranda göstermek için
	BitmapFont font2;//oyun bitti yazdırıcam

	//çarpışma
	Circle birdCircle;

	ShapeRenderer shapeRenderer;//şekil üzerine çember oluşacak ve bunlar birbirine çarpıcak

	int numberOfEnemies = 4; //4 set arı oluşturup durmadan dönecekler
	float [] enemyX = new float[numberOfEnemies];

	//arıların y eksenin rastgele yerlerde oluşturma
	float [] enemyOffset = new float[numberOfEnemies];
	float [] enemyOffset2 = new float[numberOfEnemies];
	float [] enemyOffset3 = new float[numberOfEnemies];

	float distance = 0;//arı setleri arasında ki uzaklık

	Circle[] enemyCircle;
	Circle[] enemyCircle2;
	Circle[] enemyCircle3;
	
	@Override
	public void create () {//ilk açıldığında çalışılacak şeyleri buraya ekledim
		batch = new SpriteBatch();
		background = new Texture("background.png");
		bird = new Texture("bird.png");
		bee1 = new Texture("bee.png");
		bee2 = new Texture("bee.png");
		bee3 = new Texture("bee.png");

		distance = Gdx.graphics.getWidth() / 2;//aralarındaki mesafe güncellendi

		random = new Random();

		birdX = Gdx.graphics.getWidth() / 2 - bird.getHeight() / 2;
		birdY = Gdx.graphics.getHeight() / 3;

		shapeRenderer = new ShapeRenderer();

		birdCircle = new Circle();
		enemyCircle = new Circle[numberOfEnemies];
		enemyCircle2 = new Circle[numberOfEnemies];
		enemyCircle3 = new Circle[numberOfEnemies];

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(4);

		font2 = new BitmapFont();
		font2.setColor(Color.WHITE);
		font2.getData().setScale(8);


		for(int i = 0; i<numberOfEnemies; i++){

			enemyOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
			enemyOffset2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
			enemyOffset3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

			enemyX[i] = Gdx.graphics.getWidth() - bee1.getWidth() / 2 + i*distance;

			enemyCircle[i] = new Circle();
			enemyCircle2[i] = new Circle();
			enemyCircle3[i] = new Circle();

		}
	}

	@Override
	public void render () {//oyun çalıştığında sürekli çalışacak kod
		batch.begin();//egin end arasına ne çizeceğimizi hangi objelerin olacağını yazıcam
		batch.draw(background,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		//arka planımı oluşturdum va dedim ki bu x ve y koordinatlarından başla ve
		// ekranın genişliği ve boyutu kadar ekranda oluştur

		if(gameState == 1){//oyun başladığında

			if(enemyX[scoreEnemy] < Gdx.graphics.getWidth() / 2 - bird.getHeight() / 2){
				//enemy(arı) kuşumuzdan daha geride bir yerde ise
				score++;

				if(scoreEnemy < numberOfEnemies - 1){//bizim 4 setimiz var
					scoreEnemy++;
				}else{
					scoreEnemy = 0;
				}
			}

			if(Gdx.input.justTouched()){//kuşu uçurdum
				velocity = -15;
			}

			for(int i = 0; i<numberOfEnemies;i++){

				if(enemyX[i] < 0){//0 altına düşerse değer
					//4 setten sonra sürekli devam etsin diye döngü yaptım
					enemyX[i] = enemyX[i] + numberOfEnemies * distance;

					enemyOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
					enemyOffset2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
					enemyOffset3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
				}else{
					enemyX[i] = enemyX[i] - enemyVelocity;
				}


				batch.draw(bee1,enemyX[i],Gdx.graphics.getHeight()/2 + enemyOffset[i],Gdx.graphics.getWidth() / 15,Gdx.graphics.getHeight() / 10);
				batch.draw(bee2,enemyX[i],Gdx.graphics.getHeight()/2 + enemyOffset2[i],Gdx.graphics.getWidth() / 15,Gdx.graphics.getHeight() / 10);
				batch.draw(bee3,enemyX[i],Gdx.graphics.getHeight()/2 + enemyOffset3[i],Gdx.graphics.getWidth() / 15,Gdx.graphics.getHeight() / 10);

				enemyCircle[i] = new Circle(enemyX[i]+Gdx.graphics.getWidth() / 30,Gdx.graphics.getHeight()/2 + enemyOffset[i]+ Gdx.graphics.getHeight() / 20,Gdx.graphics.getWidth() / 30);
				enemyCircle2[i] = new Circle(enemyX[i]+Gdx.graphics.getWidth() / 30,Gdx.graphics.getHeight()/2 + enemyOffset2[i]+ Gdx.graphics.getHeight() / 20,Gdx.graphics.getWidth() / 30);
				enemyCircle3[i] = new Circle(enemyX[i]+Gdx.graphics.getWidth() / 30,Gdx.graphics.getHeight()/2 + enemyOffset3[i]+ Gdx.graphics.getHeight() / 20,Gdx.graphics.getWidth() / 30);

			}

			if(birdY > 0){
		//kuş aşağı iniyor ama en alta gelince kaybolmasın orda durmasını sağladım
				velocity = velocity + gravity;
				birdY = birdY - velocity;
			}else{
				gameState = 2;

			}

		}else if(gameState == 0){//oyun başlamıdığında olacak durum
			if(Gdx.input.justTouched()){
				//kullanıcı ekrana tıklayınca kuş hareket edicek ve oyun başlayacak
				gameState = 1;//oyun başladığı için değeri 1 yaptım
			}
		}else if(gameState == 2){

			font2.draw(batch,"GAME OVER! Tap To Play Again",100,Gdx.graphics.getHeight()/2);

			if(Gdx.input.justTouched()){
				//kullanıcı ekrana tıklayınca kuş hareket edicek ve oyun başlayacak
				gameState = 1;//oyun başladığı için değeri 1 yaptım

				//en ilk konumlarına getirdim
				birdY = Gdx.graphics.getHeight() / 3;

				for(int i = 0; i<numberOfEnemies; i++){

					enemyOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
					enemyOffset2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
					enemyOffset3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

					enemyX[i] = Gdx.graphics.getWidth() - bee1.getWidth() / 2 + i*distance;

					enemyCircle[i] = new Circle();
					enemyCircle2[i] = new Circle();
					enemyCircle3[i] = new Circle();

				}
				velocity = 0;
				scoreEnemy = 0;//oyun bitince sıfırladım
				score = 0;
			}
		}



		batch.draw(bird,birdX,birdY,Gdx.graphics.getWidth() / 15,Gdx.graphics.getHeight() / 10);
		//kuşun x koordinatını sola yakın olarak oluşturmaya çalıştım ki oyun başladığında direk düşmalarla karşılaşmasın,y koordinatını da yerden yüksekliği tam ortası değilde biraz alttan başlattım,
		//sonra da kuşun boyunu ayarladım

		font.draw(batch,String.valueOf(score),100,200);

		batch.end();

		birdCircle.set(birdX+Gdx.graphics.getWidth() / 30,birdY+Gdx.graphics.getHeight() / 20,Gdx.graphics.getWidth() / 30);
		//Gdx.graphics.getWidth() / 15)/2

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.BLACK);
		//shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);


		for(int i = 0; i < numberOfEnemies; i++){
			//shapeRenderer.circle(enemyX[i]+Gdx.graphics.getWidth() / 30,Gdx.graphics.getHeight()/2 + enemyOffset[i]+ Gdx.graphics.getHeight() / 20,Gdx.graphics.getWidth() / 30);
			//shapeRenderer.circle(enemyX[i]+Gdx.graphics.getWidth() / 30,Gdx.graphics.getHeight()/2 + enemyOffset2[i]+ Gdx.graphics.getHeight() / 20,Gdx.graphics.getWidth() / 30);
			//shapeRenderer.circle(enemyX[i]+Gdx.graphics.getWidth() / 30,Gdx.graphics.getHeight()/2 + enemyOffset3[i]+ Gdx.graphics.getHeight() / 20,Gdx.graphics.getWidth() / 30);

			if(Intersector.overlaps(birdCircle,enemyCircle[i]) || Intersector.overlaps(birdCircle,enemyCircle2[i]) || Intersector.overlaps(birdCircle,enemyCircle3[i])){
				//eğer çarpışma olduysa oyunu bitiricem
				gameState = 2;
			}
		}
		//shapeRenderer.end();

	}
	
	@Override
	public void dispose () {

	}
}
