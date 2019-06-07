package org.liblouis;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

/** A simple table resolver that resolves tables in one specific local directory. */
public class DirectoryTableResolver implements TableResolver {

	private Path dir; 
	
	public DirectoryTableResolver(Path dir) {
		BasicFileAttributes a; 
		try {
			a = Files.getFileAttributeView(dir, BasicFileAttributeView.class).readAttributes(); 
		} catch (NoSuchFileException e) {
			throw new RuntimeException("directory does not exist: "+dir); 
		}		catch (FileSystemNotFoundException e) { 
			throw new RuntimeException(e); 
		}		catch (IOException e) {
			throw new RuntimeException(e); 
		}
		if (!a.isDirectory())
			throw new RuntimeException("Not a directory: "+dir);		
		this.dir = dir;
	} 
	
	@Override
	public URL resolve(String table, URL base) {
		try {
			return dir.resolve(table).toFile().toURI().toURL();
		} catch (MalformedURLException e) {
			return null; 
		}
	}

	@Override
	public Set<String> list() {
		File[] files = dir.toFile().listFiles(new FileFilter() { //NOSONAR
			@Override
			public boolean accept(File file) {
				return file.isFile(); 
			}
		});
		
		Set<String> result = new HashSet<>();
		for (File it : files) {
			result.add(it.getName());
		}
		return result; 
	}

}
