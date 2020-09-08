package pract;

//@author Charles Zhu




import java.util.Arrays;

import java.util.Scanner;

import static java.lang.System.out;

import java.io.File;

import java.lang.Math;

import java.io.FileNotFoundException;

import java.util.ArrayList;

public class ACSL3 {

	public static void main(String[] args) throws FileNotFoundException {

		File f = new File("..\\lol.txt"); // H:\\Eclipse\\Workspace\\ACSL3\\src\\ACSLInputs

		Scanner sc = new Scanner(f);


		for (int i = 0; i < 5; i++)

		{
			int[][] finalArray = new int[4][4];
			ArrayList<TwoDArray> arr = new ArrayList<TwoDArray>();

			String array[] = sc.nextLine().split("\\+");

			int[] count = new int[array.length];

			for (int c = 0; c < array.length; c++) {

				arr.add(new TwoDArray());

			}

			for (int j = 0; j < array.length; j++)

			{

				String letter = " ";

				String line = array[j];

				for (int k = 0; k < array[j].length(); k++)

				{

					if (line.charAt(k) == '~')

					{

						switch (line.charAt(k + 1))

						{

						case 'A':
							letter = "~A";

							break;

						case 'B':
							letter = "~B";

							break;

						case 'C':
							letter = "~C";

							break;

						case 'D':
							letter = "~D";

							break;

						}

						k++;

						count[j]++;

					}

					else

					{

						letter = Character.toString(line.charAt(k));

						count[j]++;

					}

					if (letter.equals("A"))

					{

						TwoDArray x = arr.get(j);

						for (int row = 0; row < 4; row++)

						{

							for (int col = 0; col < 2; col++)

							{

								x.array[row][col] += 1;

							}

						}

					}

					else if (letter.equals("~A")) {

						TwoDArray x = arr.get(j);

						for (int row = 0; row < 4; row++)

						{

							for (int col = 2; col < 4; col++)

							{

								x.array[row][col] += 1;

							}

						}

					}

					else if (letter.equals("B"))

					{

						TwoDArray x = arr.get(j);

						for (int row = 0; row < 2; row++)

						{

							for (int col = 0; col < 4; col++)

							{

								x.array[row][col] += 1;

							}

						}

					}

					else if (letter.equals("~B"))

					{

						TwoDArray x = arr.get(j);

						for (int row = 2; row < 4; row++)

						{

							for (int col = 0; col < 4; col++)

							{

								x.array[row][col] += 1;

							}

						}

					}

					else if (letter.equals("C"))

					{

						TwoDArray x = arr.get(j);

						for (int row = 0; row < 4; row++)

						{

							for (int col = 1; col < 3; col++)

							{

								x.array[row][col] += 1;

							}

						}

					}

					else if (letter.equals("~C"))

					{

						TwoDArray x = arr.get(j);

						for (int row = 0; row < 4; row++)

						{

							x.array[row][0] += 1;

							x.array[row][3] += 1;

						}

					}

					else if (letter.equals("D"))

					{

						TwoDArray x = arr.get(j);

						for (int row = 1; row < 3; row++)

						{

							for (int col = 0; col < 4; col++)

							{

								x.array[row][col] = 1;

							}

						}

					}

					else if (letter.equals("~D"))

					{

						TwoDArray x = arr.get(j);

						for (int col = 0; col < 4; col++)

						{

							x.array[0][col] += 1;

							x.array[3][col] += 1;

						}

					}

				}

			}

			for (int j = 0; j < arr.size(); j++) {

				int r[][] = arr.get(j).array;

				for (int row = 0; row < r.length; row++) {

					for (int column = 0; column < r[row].length; column++) {

						if (r[row][column] == count[j]) {

							finalArray[row][column] = 1;

						}

					}

				}

			}


			String line = "";

			for (int row = 0; row < finalArray.length; row++) {

				line = "";

				for (int col = 0; col < finalArray[row].length; col++) {

					line += finalArray[row][col];

				}

				// System.out.println("line: " + line);

				int decimal = Integer.parseInt(line, 2);

				String hex = Integer.toString(decimal, 16);

				System.out.print(hex.toUpperCase());

			}
			System.out.println();

		}

	}

}

class TwoDArray {

	int[][] array = new int[4][4];

	public TwoDArray()

	{

	}

}