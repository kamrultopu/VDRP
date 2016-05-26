/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vdrp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author misty
 */
public class Uploader extends HttpServlet {
    static final int BUFFER_SIZE = 64;
    static final String SUCCESS = "SUCCESS";
    static final String ERROR = "ERROR";
    static final String SAVE_DIR = "C:\\Users\\kamrul\\Desktop\\VDRP\\VDRP\\VDRPServer\\FileFolder\\";
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String userName = request.getHeader("username");
        String fileName = request.getHeader("fileName");
        String bucketName = request.getHeader("bucketName");
        String keyName = request.getHeader("keyName");
        String accessKey = request.getHeader("accessKey");
        String secoption = request.getHeader("secoption");
        String nCopy = request.getHeader("copy");
        
        int index = fileName.indexOf(".txt");
        String ext = fileName.substring(index);
        // opens input stream of the request for reading data
        InputStream inputStream = request.getInputStream();
        
        String tmpFileName = SAVE_DIR + fileName.substring(0,index) + nCopy + ext;
        File saveFile = new File(tmpFileName);
        FileOutputStream outputStream = new FileOutputStream(saveFile);
        
         
        // opens an output stream for writing file
        //FileOutputStream outputStream = new FileOutputStream(saveFile);
         
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;
        System.out.println("Receiving data...");
         
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
         
        System.out.println("Data received.");
        outputStream.close();
        
        inputStream.close();
         
        //System.out.println("File written to: " + saveFile.getAbsolutePath());
         
        
        String retResponse = "SUCCESS";
        
        
        response.getWriter().print(retResponse);
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
