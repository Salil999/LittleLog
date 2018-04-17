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
        this(100.0, 5, sourceDirectory, destinationDirectory);
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

	public static String getFilePathWithoutExtension(final File file) {
		final String fileName = file.getName();
		final int position = fileName.lastIndexOf('.');
		if (position > 0) {
			return fileName.substring(0, position);
		}
		return fileName;
	}

	public static void shardFile(final File fileToShard, final double chunkSize) {
		int chunkNumber = 0;
		System.out.println("Sharding " + fileToShard.getName());
		System.out.println("Creating chunk 0");
		final String fileName = getFilePathWithoutExtension(fileToShard);

		final File pathname = new File(fileToShard.getParent() + "/" + fileName + "_log/");
		System.out.println(pathname);
		deleteIfExists(pathname);
		pathname.mkdirs();

		try {
			final BufferedReader reader = new BufferedReader(new FileReader(fileToShard));
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				final File shardedChunk = new File(pathname.getAbsolutePath() + "/"
						+ fileName + "_" + chunkNumber + ".log");
				final FileWriter writer = new FileWriter(shardedChunk, true);
				writer.write(line + '\n');
				writer.close();

				if (shardedChunk.length() > chunkSize * Sharder.ONE_MB) {
					chunkNumber++;
					System.out.println("Creating chunk " + chunkNumber);
				}
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private static void deleteIfExists(final File directory) {
		final File[] allContents = directory.listFiles();
		if (allContents != null) {
			for (final File file : allContents) {
				deleteIfExists(file);
			}
		}
		directory.delete();
	}

	public void shardDirectory() {
		for (final File file : this.sourceDirectory.listFiles()) {
			// Create the output directory for the current file
			final String fileNoExtension = Sharder.getFilePathWithoutExtension(file);
			final File outputDirectoryWithFilename = new File(Sharder.this.destinationDirectory + "/" + fileNoExtension);

			deleteIfExists(outputDirectoryWithFilename);
			outputDirectoryWithFilename.mkdirs();

			this.pool.execute(() -> {
				// Shard the file once the directory has been created
				try {
                    System.out.println("Sharding " + file.getName());
					Sharder.this.shardFile(file, outputDirectoryWithFilename, fileNoExtension);
					System.out.println("Finished sharding " + file.getName());
				} catch (final IOException e) {
					e.printStackTrace();
					System.err.println("Sharding failed for file: " + file.getAbsolutePath());
				}
			});
		}
        this.pool.shutdown();
	}

	public void shardFile(final File fileToShard, final File specificFileOutputDirectory, final String fileName) throws
			IOException {
		final BufferedReader reader = new BufferedReader(new FileReader(fileToShard));
		String line;
		int chunkNumber = 0;
		System.out.println("Creating chunk 0");
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
				System.out.println("Creating chunk " + chunkNumber);
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
