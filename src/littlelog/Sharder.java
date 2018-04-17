package littlelog;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Sharder {

	public static final double ONE_MB = 1e6;
	private final double chunkSize;
	private final ExecutorService pool;
	private File sourceDirectory;
	private File destinationDirectory;

	public Sharder(final File sourceDirectory, final File destinationDirectory) {
		// TODO: We need to find the "optimal" value for tuning and set that to be the default
		// For now, assume 50MB - will change for future commits
		this(50.0, 5, sourceDirectory, destinationDirectory);
	}

	public Sharder(final double chunkSize, final int maxThreads, final File sourceDirectory, final File
			destinationDirectory) {
		this.chunkSize = chunkSize * Sharder.ONE_MB;
		this.pool = Executors.newFixedThreadPool(maxThreads);
		this.sourceDirectory = sourceDirectory;
		this.destinationDirectory = destinationDirectory;
	}

	public Sharder(final double chunkSize, final File sourceDirectory, final File destinationDirectory) {
		this(chunkSize, 5, sourceDirectory, destinationDirectory);
	}

	public void shardDirectory() throws IOException {
		for (final File file : this.sourceDirectory.listFiles()) {
			// Create the output directory for the current file
			final String fileNoExtension = Sharder.getFilePathWithoutExtension(file);
			final File outputDirectoryWithFilename = new File(Sharder.this.destinationDirectory + "/" + fileNoExtension);
			outputDirectoryWithFilename.mkdirs();
			this.pool.execute(() -> {
				// Shard the file once the directory has been created
				try {
					Sharder.this.shardFile(file, outputDirectoryWithFilename, fileNoExtension);
				} catch (final IOException e) {
					e.printStackTrace();
					System.err.println("Sharding failed for file: " + file.getAbsolutePath());
				}
			});
		}
	}

	public static String getFilePathWithoutExtension(final File file) {
		final String fileName = file.getName();
		final int position = fileName.lastIndexOf('.');
		if (position > 0) {
			return fileName.substring(0, position);
		}
		return fileName;
	}

	public void shardFile(final File fileToShard, final File specificFileOutputDirectory, final String fileName) throws
			IOException {
		final BufferedReader reader = new BufferedReader(new FileReader(fileToShard));
		String line;
		int chunkNumber = 0;
		while ((line = reader.readLine()) != null) {
			// This is the file we're going to write to - based on the current chunk number
			final File shardedChunk = new File(specificFileOutputDirectory.getAbsolutePath() + "/"
					+ fileName
					+ "_" + chunkNumber + ".log");

			// Make sure we keep appending to the chunk
			final FileWriter writer = new FileWriter(shardedChunk, true);
			writer.write(line + '\n');
			writer.close();

			// Update the chunk number if we hit the max chunk size for the current chunk number
			if (shardedChunk.length() > this.chunkSize) {
				chunkNumber++;
			}
		}
	}

	public double getChunkSize() {
		return this.chunkSize;
	}

	public File getSourceDirectory() {
		return this.sourceDirectory;
	}

	public void setSourceDirectory(final File sourceDirectory) {
		this.sourceDirectory = sourceDirectory;
	}

	public File getDestinationDirectory() {
		return this.destinationDirectory;
	}

	public void setDestinationDirectory(final File destinationDirectory) {
		this.destinationDirectory = destinationDirectory;
	}
}
