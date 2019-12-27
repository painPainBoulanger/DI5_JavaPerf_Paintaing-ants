package org.polytechtours.javaperformance.tp.paintingants;
// package PaintingAnts_v2;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

// version : 2.0


/**
 * <p>
 * Titre : Painting Ants
 * </p>
 * <p>
 * Description :
 * </p>
 * <p>
 * Copyright : Copyright (c) 2003
 * </p>
 * <p>
 * Société : Equipe Réseaux/TIC - Laboratoire d'Informatique de l'Université de
 * Tours
 * </p>
 *
 * @author Nicolas Monmarché
 * @version 1.0
 */

public class CPainting extends Canvas implements MouseListener {
    private static final long serialVersionUID = 1L;
    // matrice servant pour le produit de convolution
    static private float[][] mMatriceConv9 = new float[3][3];
    static private float[][] mMatriceConv25 = new float[5][5];
    static private float[][] mMatriceConv49 = new float[7][7];
    // Objet de type Graphics permettant de manipuler l'affichage du Canvas
    private Graphics mGraphics;
    // Objet ne servant que pour les bloc synchronized pour la manipulation du
    // tableau des couleurs
    private Object mMutexCouleurs = new Object();
    // tableau des couleurs, il permert de conserver en memoire l'état de chaque
    // pixel du canvas, ce qui est necessaire au deplacemet des fourmi
    // il sert aussi pour la fonction paint du Canvas
    private Color[][] mCouleurs;
    // couleur du fond
    private Color mCouleurFond = new Color(255, 255, 255);
    // dimensions
    private Dimension mDimension = new Dimension();

    private PaintingAnts mApplis;

    private boolean mSuspendu = false;

    private Map<Integer, Color> cacheCouleur;

    /******************************************************************************
     * Titre : public CPainting() Description : Constructeur de la classe
     ******************************************************************************/
    public CPainting(Dimension pDimension, PaintingAnts pApplis) {
        int i, j;
        addMouseListener(this);

        mApplis = pApplis;

        mDimension = pDimension;
        setBounds(new Rectangle(0, 0, mDimension.width, mDimension.height));

        this.setBackground(mCouleurFond);

        // initialisation de la matrice des couleurs
        mCouleurs = new Color[mDimension.width][mDimension.height];
        synchronized (mMutexCouleurs) {
            for (i = 0; i != mDimension.width; i++) {
                for (j = 0; j != mDimension.height; j++) {
                    mCouleurs[i][j] = new Color(mCouleurFond.getRed(), mCouleurFond.getGreen(), mCouleurFond.getBlue());
                }
            }
        }

        cacheCouleur = new HashMap<>();

    }

    /******************************************************************************
     * Titre : Color getCouleur Description : Cette fonction renvoie la couleur
     * d'une case
     ******************************************************************************/
    public Color getCouleur(int x, int y) {
        synchronized (mMutexCouleurs) {
            return mCouleurs[x][y];
        }
    }

    /******************************************************************************
     * Titre : Color getDimension Description : Cette fonction renvoie la
     * dimension de la peinture
     ******************************************************************************/
    public Dimension getDimension() {
        return mDimension;
    }

    /******************************************************************************
     * Titre : Color getHauteur Description : Cette fonction renvoie la hauteur de
     * la peinture
     ******************************************************************************/
    public int getHauteur() {
        return mDimension.height;
    }

    /******************************************************************************
     * Titre : Color getLargeur Description : Cette fonction renvoie la hauteur de
     * la peinture
     ******************************************************************************/
    public int getLargeur() {
        return mDimension.width;
    }

    /******************************************************************************
     * Titre : void init() Description : Initialise le fond a la couleur blanche
     * et initialise le tableau des couleurs avec la couleur blanche
     ******************************************************************************/
    public void init() {
        int i, j;
        mGraphics = getGraphics();
        synchronized (mMutexCouleurs) {
            mGraphics.clearRect(0, 0, mDimension.width, mDimension.height);

            // initialisation de la matrice des couleurs

            for (i = 0; i != mDimension.width; i++) {
                for (j = 0; j != mDimension.height; j++) {
                    mCouleurs[i][j] = new Color(mCouleurFond.getRed(), mCouleurFond.getGreen(), mCouleurFond.getBlue());
                }
            }
        }

        // initialisation de la matrice de convolution : lissage moyen sur 9
        // cases
        /*
         * 1 2 1 2 4 2 1 2 1
         */
        CPainting.mMatriceConv9[0][0] = 1 / 16f;
        CPainting.mMatriceConv9[0][1] = 2 / 16f;
        CPainting.mMatriceConv9[0][2] = 1 / 16f;
        CPainting.mMatriceConv9[1][0] = 2 / 16f;
        CPainting.mMatriceConv9[1][1] = 4 / 16f;
        CPainting.mMatriceConv9[1][2] = 2 / 16f;
        CPainting.mMatriceConv9[2][0] = 1 / 16f;
        CPainting.mMatriceConv9[2][1] = 2 / 16f;
        CPainting.mMatriceConv9[2][2] = 1 / 16f;

        // initialisation de la matrice de convolution : lissage moyen sur 25
        // cases
        /*
         * 1 1 2 1 1 1 2 3 2 1 2 3 4 3 2 1 2 3 2 1 1 1 2 1 1
         */
        CPainting.mMatriceConv25[0][0] = 1 / 44f;
        CPainting.mMatriceConv25[0][1] = 1 / 44f;
        CPainting.mMatriceConv25[0][2] = 2 / 44f;
        CPainting.mMatriceConv25[0][3] = 1 / 44f;
        CPainting.mMatriceConv25[0][4] = 1 / 44f;
        CPainting.mMatriceConv25[1][0] = 1 / 44f;
        CPainting.mMatriceConv25[1][1] = 2 / 44f;
        CPainting.mMatriceConv25[1][2] = 3 / 44f;
        CPainting.mMatriceConv25[1][3] = 2 / 44f;
        CPainting.mMatriceConv25[1][4] = 1 / 44f;
        CPainting.mMatriceConv25[2][0] = 2 / 44f;
        CPainting.mMatriceConv25[2][1] = 3 / 44f;
        CPainting.mMatriceConv25[2][2] = 4 / 44f;
        CPainting.mMatriceConv25[2][3] = 3 / 44f;
        CPainting.mMatriceConv25[2][4] = 2 / 44f;
        CPainting.mMatriceConv25[3][0] = 1 / 44f;
        CPainting.mMatriceConv25[3][1] = 2 / 44f;
        CPainting.mMatriceConv25[3][2] = 3 / 44f;
        CPainting.mMatriceConv25[3][3] = 2 / 44f;
        CPainting.mMatriceConv25[3][4] = 1 / 44f;
        CPainting.mMatriceConv25[4][0] = 1 / 44f;
        CPainting.mMatriceConv25[4][1] = 1 / 44f;
        CPainting.mMatriceConv25[4][2] = 2 / 44f;
        CPainting.mMatriceConv25[4][3] = 1 / 44f;
        CPainting.mMatriceConv25[4][4] = 1 / 44f;

        // initialisation de la matrice de convolution : lissage moyen sur 49
        // cases
        /*
         * 1 1 2 2 2 1 1 1 2 3 4 3 2 1 2 3 4 5 4 3 2 2 4 5 8 5 4 2 2 3 4 5 4 3 2 1 2
         * 3 4 3 2 1 1 1 2 2 2 1 1
         */
        CPainting.mMatriceConv49[0][0] = 1 / 128f;
        CPainting.mMatriceConv49[0][1] = 1 / 128f;
        CPainting.mMatriceConv49[0][2] = 2 / 128f;
        CPainting.mMatriceConv49[0][3] = 2 / 128f;
        CPainting.mMatriceConv49[0][4] = 2 / 128f;
        CPainting.mMatriceConv49[0][5] = 1 / 128f;
        CPainting.mMatriceConv49[0][6] = 1 / 128f;

        CPainting.mMatriceConv49[1][0] = 1 / 128f;
        CPainting.mMatriceConv49[1][1] = 2 / 128f;
        CPainting.mMatriceConv49[1][2] = 3 / 128f;
        CPainting.mMatriceConv49[1][3] = 4 / 128f;
        CPainting.mMatriceConv49[1][4] = 3 / 128f;
        CPainting.mMatriceConv49[1][5] = 2 / 128f;
        CPainting.mMatriceConv49[1][6] = 1 / 128f;

        CPainting.mMatriceConv49[2][0] = 2 / 128f;
        CPainting.mMatriceConv49[2][1] = 3 / 128f;
        CPainting.mMatriceConv49[2][2] = 4 / 128f;
        CPainting.mMatriceConv49[2][3] = 5 / 128f;
        CPainting.mMatriceConv49[2][4] = 4 / 128f;
        CPainting.mMatriceConv49[2][5] = 3 / 128f;
        CPainting.mMatriceConv49[2][6] = 2 / 128f;

        CPainting.mMatriceConv49[3][0] = 2 / 128f;
        CPainting.mMatriceConv49[3][1] = 4 / 128f;
        CPainting.mMatriceConv49[3][2] = 5 / 128f;
        CPainting.mMatriceConv49[3][3] = 8 / 128f;
        CPainting.mMatriceConv49[3][4] = 5 / 128f;
        CPainting.mMatriceConv49[3][5] = 4 / 128f;
        CPainting.mMatriceConv49[3][6] = 2 / 128f;

        CPainting.mMatriceConv49[4][0] = 2 / 128f;
        CPainting.mMatriceConv49[4][1] = 3 / 128f;
        CPainting.mMatriceConv49[4][2] = 4 / 128f;
        CPainting.mMatriceConv49[4][3] = 5 / 128f;
        CPainting.mMatriceConv49[4][4] = 4 / 128f;
        CPainting.mMatriceConv49[4][5] = 3 / 128f;
        CPainting.mMatriceConv49[4][6] = 2 / 128f;

        CPainting.mMatriceConv49[5][0] = 1 / 128f;
        CPainting.mMatriceConv49[5][1] = 2 / 128f;
        CPainting.mMatriceConv49[5][2] = 3 / 128f;
        CPainting.mMatriceConv49[5][3] = 4 / 128f;
        CPainting.mMatriceConv49[5][4] = 3 / 128f;
        CPainting.mMatriceConv49[5][5] = 2 / 128f;
        CPainting.mMatriceConv49[5][6] = 1 / 128f;

        CPainting.mMatriceConv49[6][0] = 1 / 128f;
        CPainting.mMatriceConv49[6][1] = 1 / 128f;
        CPainting.mMatriceConv49[6][2] = 2 / 128f;
        CPainting.mMatriceConv49[6][3] = 2 / 128f;
        CPainting.mMatriceConv49[6][4] = 2 / 128f;
        CPainting.mMatriceConv49[6][5] = 1 / 128f;
        CPainting.mMatriceConv49[6][6] = 1 / 128f;

        mSuspendu = false;
    }

    /****************************************************************************/
    public void mouseClicked(MouseEvent pMouseEvent) {
        pMouseEvent.consume();
        if (pMouseEvent.getButton() == MouseEvent.BUTTON1) {
            // double clic sur le bouton gauche = effacer et recommencer
            if (pMouseEvent.getClickCount() == 2) {
                init();
            }
            // simple clic = suspendre les calculs et l'affichage
            mApplis.pause();
        } else {
            // bouton du milieu (roulette) = suspendre l'affichage mais
            // continuer les calculs
            if (pMouseEvent.getButton() == MouseEvent.BUTTON2) {
                suspendre();
            } else {
                // clic bouton droit = effacer et recommencer
                // case pMouseEvent.BUTTON3:
                init();
            }
        }
    }

    /****************************************************************************/
    public void mouseEntered(MouseEvent pMouseEvent) {
    }

    /****************************************************************************/
    public void mouseExited(MouseEvent pMouseEvent) {
    }

    /****************************************************************************/
    public void mousePressed(MouseEvent pMouseEvent) {

    }

    /****************************************************************************/
    public void mouseReleased(MouseEvent pMouseEvent) {
    }

    /******************************************************************************
     * Titre : void paint(Graphics g) Description : Surcharge de la fonction qui
     * est appelé lorsque le composant doit être redessiné
     ******************************************************************************/
    @Override
    public void paint(Graphics pGraphics) {
        int i, j;

        synchronized (mMutexCouleurs) {
            for (i = 0; i < mDimension.width; i++) {
                for (j = 0; j < mDimension.height; j++) {
                    pGraphics.setColor(mCouleurs[i][j]);
                    pGraphics.fillRect(i, j, 1, 1);
                }
            }
        }
    }

    /**
     * Méthode implémentant un système de cache pour les couleurs
     * @param r valeur rouge
     * @param g valeur verte
     * @param b valeur bleu
     * @return Si la couleur existe déja, retourne objet couleur correspondant dans la hashMap,
     *          si il existe pas, création de l'objet couleur et retourne cet objet
     */
    private Color traiterCouleur(int r, int g, int b) {

        int value = (0xFF000000) |
                    ((r & 0xFF) << 16) |
                    ((g & 0xFF) << 8)  |
                    ((b & 0xFF) << 0);

        Color color = cacheCouleur.get(value);

        if (color == null) {
            color = new Color((int) r, (int) g, (int) b);
            cacheCouleur.put(value, color);
        }

        return color;
    }

    /**
     * Méthode permettant de calculer les valeurs r g b
     * @param pTaille taille de la matrice de convolution
     * @param x coordonnée en x de la fourmi
     * @param y coordonnée en y de la fourmi
     */
    private void computeCouleur(int pTaille, int x, int y) {
        int i, j, k, l, m, n;
        float R, G, B;
        Color lColor;
        int tailleConvo = 2*pTaille + 1;
        float [][] matriceConvo;
        if (pTaille == 0) {
            // on ne fait rien = pas de diffusion
            return;
        }else if (pTaille == 1) {
            matriceConvo = CPainting.mMatriceConv9;
        } else if (pTaille == 2) {
            matriceConvo = CPainting.mMatriceConv25;
        } else if (pTaille == 3) {
            matriceConvo = CPainting.mMatriceConv49;
        } else {
            throw new IllegalArgumentException("pTaille n'est pas une valeur possible");
        }

        // produit de convolution discrete sur tailleConvo * tailleConvo cases
        for (i = 0; i < tailleConvo; i++) {
            for (j = 0; j < tailleConvo; j++) {
                R = G = B = 0f;

                for (k = 0; k < tailleConvo; k++) {
                    for (l = 0; l < tailleConvo; l++) {
                        m = (x + i + k - (2*pTaille) + mDimension.width) % mDimension.width;
                        n = (y + j + l - (2*pTaille) + mDimension.height) % mDimension.height;
                        R += matriceConvo[k][l] * mCouleurs[m][n].getRed();
                        G += matriceConvo[k][l] * mCouleurs[m][n].getGreen();
                        B += matriceConvo[k][l] * mCouleurs[m][n].getBlue();
                    }
                }

                lColor = traiterCouleur((int)R,(int) G, (int)B);
                mGraphics.setColor(lColor);

                m = (x + i - pTaille + mDimension.width) % mDimension.width;
                n = (y + j - pTaille + mDimension.height) % mDimension.height;
                mCouleurs[m][n] = lColor;
                if (!mSuspendu) {
                    mGraphics.fillRect(m, n, 1, 1);
                }
            }
        }
    }

    /******************************************************************************
     * Titre : void colorer_case(int x, int y, Color c) Description : Cette
     * fonction va colorer le pixel correspondant et mettre a jour le tabmleau des
     * couleurs
     ******************************************************************************/
    public void setCouleur(int x, int y, Color c, int pTaille) {

        synchronized (mMutexCouleurs) {
            if (!mSuspendu) {
                // on colorie la case sur laquelle se trouve la fourmi
                mGraphics.setColor(c);
                mGraphics.fillRect(x, y, 1, 1);
            }

            mCouleurs[x][y] = c;
            computeCouleur(pTaille, x, y);
        }
    }

    /******************************************************************************
     * Titre : setSupendu Description : Cette fonction change l'état de suspension
     ******************************************************************************/
    public void suspendre() {
        mSuspendu = !mSuspendu;
        if (!mSuspendu) {
            repaint();
        }
    }
}
