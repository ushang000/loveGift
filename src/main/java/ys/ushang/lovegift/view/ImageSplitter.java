package ys.ushang.lovegift.view;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.util.Log;

public class ImageSplitter
{
	/**
	 * 将图片切成 , piece *piece
	 * 
	 * @param bitmap
	 * @param piece
	 * @return
	 */
	public static List<ImagePiece> split(Bitmap bitmap, int piece)
	{

		List<ImagePiece> pieces = new ArrayList<ImagePiece>(piece * piece);

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		int pieceWidth = width / piece;
		int pieceHeight=height/piece;

		for (int i = 0; i < piece; i++)
		{
			for (int j = 0; j < piece; j++)
			{
				ImagePiece imagePiece = new ImagePiece();
				imagePiece.index = j + i * piece;
				
				int xValue = j * pieceWidth;
				int yValue = i * pieceHeight;
				
				imagePiece.bitmap = Bitmap.createBitmap(bitmap, xValue, yValue,
						pieceWidth, pieceHeight);
				pieces.add(imagePiece);
			}
		}
		return pieces;
	}
}
