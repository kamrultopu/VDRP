/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vdrp;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

//import javaxserServletContextimport javax.servlet.ServletException;
//Stream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
/**
 *
 * @author misty
 */
public class Access extends HttpServlet {
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
        
        String fileName = request.getHeader("fileName");
        String bucketName = request.getHeader("bucketName");
        String keyName = request.getHeader("keyName");
        String accessKey = request.getHeader("accessKey");
        String secoption = request.getHeader("secoption");
        String nCopy = request.getHeader("copy");
        
        int copy = Integer.parseInt(nCopy);
        
        response.setContentType("text/plain");
        response.setHeader("Content-disposition", "attachment;filename="+fileName);
        int index = fileName.indexOf(".txt");
        String ext = fileName.substring(index);
        String fromFileName = SAVE_DIR + fileName.substring(0,index) + nCopy + ext ;//+ Integer.toString(1);
        File fromFile = new File(fromFileName);
        FileInputStream inputStream = new FileInputStream(fromFile);
        ServletContext context = getServletContext();
        String mimeType = context.getMimeType(fromFileName);
        if(mimeType == null ){
            mimeType = "application/octet-stream";
        }
        response.setContentType(mimeType);
        response.setContentLength((int)fromFile.length());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" +fromFile.getName();
        response.setHeader(headerKey, headerValue);
        OutputStream outputStream = response.getOutputStream();
        if( inputStream != null ){
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
            while((bytesRead = inputStream.read(buffer)) != -1 ){
                int n = bytesRead;
                outputStream.write(buffer,0,bytesRead);
            }
        }
        inputStream.close();
        outputStream.close();
        
        String retResponse = "SUCCESS";
        
        
        //response.getWriter().print(retResponse);
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
