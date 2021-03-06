﻿import java.util.Random;



public class Genetico {

	public static void resultado(int[][] cromossomos, int[] resultados, String[] GASTOS) {
		int i, i2;
		i=0;

		for (i2 = 0; i2 < Principal.getGASTOS(); i2++) {
			System.out.println(GASTOS[cromossomos[i][i2]] + " = "+ resultados[i2]);
		}
		System.out.println(" ");
		System.out.println("Resultado    = " + resultados[i]);
        }
        //renova os cromossomos possibilitando melhoras
	public static void renovarCromossomos(int[][] cromossomos,
			int[] resultados, float taxaSobrevivencia) {

		int inicioExcluidos = (int) (taxaSobrevivencia * 10);

		int i, i2 = 0;

		for (i = inicioExcluidos; i < 10; i++) {

			boolean valido = false;

			while (!valido) {

				int[] c_tmp = resetaCromossomo();

				// pegando 2 pais aleatoriamente
				int pai1, pai2;

				pai1 = new Random().nextInt(inicioExcluidos);
				do {
					pai2 = new Random().nextInt(inicioExcluidos);
				} while ((pai1 == pai2)
						&& (resultados[pai1] != resultados[pai2]));

				// pegando 3 caracteristicas do pai 1 aleatoriamente
				for (i2 = 0; i2 < (int) Principal.getGASTOS() / 4; i2++) {
					int pos;
					pos = new Random().nextInt(Principal.getGASTOS());
					c_tmp[pos] = cromossomos[pai1][pos];
				}
				// pegando restante do pai 2
				for (i2 = 0; i2 < (int) Principal.getGASTOS() / 4; i2++) {
					int pos = new Random().nextInt(Principal.getGASTOS());
					if (c_tmp[pos] == -1) {
						if (valorValidoNoCromossomo(cromossomos[pai2][pos],	c_tmp)) {
							c_tmp[pos] = cromossomos[pai2][pos];
						}
					}
				}

				// preenchendo o restante com aleatorios
				for (i2 = 0; i2 < Principal.getGASTOS(); i2++) {
					if (c_tmp[i2] == -1) {
						int crom_temp = valorValidoNoCromossomo(c_tmp);
						c_tmp[i2] = crom_temp;
					}
				}

				// verificando se é valido
				valido = cromossomoValido(c_tmp, cromossomos);
				if (valido) {
					cromossomos[i] = c_tmp;
				}
			}
		}

	}
        //Gera um cromossomo com caracteristicas quaisquer desde que valido
	public static int[][] gerarCromossomosAleatoriamente(int[][] cromossomos) {

		// inicializando cromossomos aleatoriamente
		int[] c_tmp = new int[Principal.getGASTOS()];

		int i, i2;
		for (i = 0; i < Principal.getNUMERO_POPULACAO(); i++) {
			boolean crom_valido = false;
			while (!crom_valido) {
				crom_valido = true;
				c_tmp = resetaCromossomo();

				// gerando cromossomo - ok
				for (i2 = 0; i2 < Principal.getGASTOS(); i2++) {
					c_tmp[i2] = valorValidoNoCromossomo(c_tmp);
				}
				crom_valido = cromossomoValido(c_tmp, cromossomos);
			}
			cromossomos[i] = c_tmp;
		}
		return cromossomos;
	}
        
	private static int[] resetaCromossomo() {
		int[] c = new int[Principal.getGASTOS()];
		int i;
		for (i = 0; i < Principal.getGASTOS(); i++) {
			c[i] = -1;
		}
		return c;
	}

	private static int valorValidoNoCromossomo(int[] c_tmp) {
		int crom_temp;
		boolean valido;
		do {
			crom_temp = new Random().nextInt(Principal.getGASTOS());
			valido = true;
			for (int ii = 0; ii < Principal.getGASTOS(); ii++) {
				if (c_tmp[ii] == crom_temp)
					valido = false;
			}
		} while (!valido);
		return crom_temp;
	}

	private static boolean valorValidoNoCromossomo(int valor, int[] c_tmp) {
		int crom_temp = valor;
		boolean valido;

		valido = true;
		for (int ii = 0; ii < Principal.getGASTOS(); ii++) {
			if (c_tmp[ii] == crom_temp)
				valido = false;
		}

		return valido;
	}

	private static boolean cromossomoValido(int[] c_tmp, int[][] cromossomos) {
		int j, j2;
		boolean crom_valido = true;

		for (j = 0; j < Principal.getNUMERO_POPULACAO(); j++) {
			int n_iguais = 0;
			for (j2 = 0; j2 < Principal.getGASTOS(); j2++) {
				if (c_tmp[j2] == cromossomos[j][j2]) {
					n_iguais++;
				}
			}

			if (n_iguais == Principal.getGASTOS())
				crom_valido = false;
		}
		return crom_valido;
	}

	public static void imprimir(int[][] cromossomos, int[] resultados,
			String[] GASTOS) {
		int i, i2;
		for (i = 0; i < Principal.getNUMERO_POPULACAO(); i++) {
			for (i2 = 0; i2 < Principal.getGASTOS(); i2++) {
				System.out.print(GASTOS[cromossomos[i][i2]] + " => ");
			}
			System.out.println(" Resultados: " + resultados[i]);
		}
	}

	public static void calcularResultado(int[][] cromossomos,
			int[] resultados, int[][] mapa) {
		int i, i2;
		// calculando o resultado
		for (i = 0; i < Principal.getNUMERO_POPULACAO(); i++) {
			int resTmp = 0;
			for (i2 = 0; i2 < Principal.getGASTOS() - 1; i2++) {
				resTmp += mapa[cromossomos[i][i2]][cromossomos[i][i2 + 1]];
			}
			resTmp+=mapa[cromossomos[i][0]][cromossomos[i][i2]];
			resultados[i] = resTmp;
		}

	}

	public static void ordenar(int[][] cromossomos, int[] resultados) {
		// ordenacao
		int i, i2;
		for (i = 0; i < 10; i++) {
			for (i2 = i; i2 < 10; i2++) {
				if (resultados[i] > resultados[i2]) {
					int vTmp;
					int[] vvTmp = new int[10];
					vTmp = resultados[i];
					resultados[i] = resultados[i2];
					resultados[i2] = vTmp;

					vvTmp = cromossomos[i];
					cromossomos[i] = cromossomos[i2];
					cromossomos[i2] = vvTmp;
				}
			}
		}
	}

}
