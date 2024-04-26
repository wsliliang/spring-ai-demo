import chromadb
chroma_client = chromadb.HttpClient(host='localhost', port=8000)

# 列出所有集合
collections = chroma_client.list_collections()

# 删除每个集合
for collection in collections:
    print(collection)
    chroma_client.delete_collection(name=collection.name)

for collection_name in collections:
    print(collection_name)