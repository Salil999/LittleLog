import matplotlib.pyplot as plt

# x = [5, 10, 20, 50, 100, 200]
# y = [1100, 522, 536, 558, 560, 545]
# plt.figure(figsize=(10, 6))
# plt.title('File Sharding Size vs. Compression Ratio')
# plt.xlabel('File Sharding Size (MB)')
# plt.ylabel('Compression Ratio (%)')
# plt.plot(x, y)
# plt.xticks(x)
# plt.yticks(list(range(100, 1000, 100)))
# plt.show()


x = [5, 10, 20, 50, 100, 200]
y = [1100, 522, 536, 558, 560, 545]
plt.figure(figsize=(10, 6))
plt.title('File Sharding Size vs. Compression Ratio')
plt.xlabel('File Sharding Size (MB)')
plt.ylabel('Compression Ratio (%)')
plt.plot(x, y)
plt.xticks(x)
plt.yticks(list(range(100, 1000, 100)))
plt.show()
