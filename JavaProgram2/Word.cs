using System;
	public class Word
	{
		private String word;
		private int quant;
		public Word(String s)
		{
			this.word = s;
			quant = 1;
		}

		int getCount()
		{
			return quant;
		}

		String getWord()
		{
			return word;
		}

		Boolean isWord(String word)
		{
			return word.Equals(this.word);
		}

		Boolean isWordIgnoreCase(String word)
		{
			return word.Equals(this.word, StringComparison.OrdinalIgnoreCase);
		}

		void AddOne()
		{
			quant++;
		}

		void print()
		{
			Console.WriteLine(word + "\t" + count);
		}

		int FindWord(Word[] list, String word, int n)
		{
			int i = 0;

			while (i < n && !list[i].word.Equals(this.word, StringComparison.OrdinalIgnoreCase))
			{
				i++;
			}

			if (i < n && list[i].word.Equals(this.word, StringComparison.OrdinalIgnoreCase))
			{
				return i;
			}
			else
			{
				return -1;
			}
		}
	}


