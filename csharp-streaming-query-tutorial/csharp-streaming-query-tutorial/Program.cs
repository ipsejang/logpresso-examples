using System;
using System.Collections.Generic;
using System.Threading;
using Araqne.Logdb.Client;

namespace Logpresso.Examples
{
    class StreamingQueryExample
    {
        [STAThread]
        static void Main(string[] args)
        {
            LogDbClient client = new LogDbClient();
            ResultCounter counter = new ResultCounter();
            long begin = System.DateTime.Now.Ticks;
            int queryId = 0;

            try
            {
                client.Connect("localhost", 8888, "araqne", "");
                // createQuery 수행 시 StreamingResultSet 인스턴스를 전달합니다.
                queryId = client.CreateQuery("table iis", counter);
                client.StartQuery(queryId);

                // 스트리밍이 완료될 때까지 대기합니다.
                do
                {
                    counter.latch.Wait(100);
                }
                while (counter.latch.CurrentCount > 0);

                if (client != null)
                {
                    if (queryId != 0)
                        client.RemoveQuery(queryId);

                    long end = System.DateTime.Now.Ticks;
                    long elapsed = (end - begin) / 10000;
                    long throughput = counter.count * 1000 / elapsed;

                    Console.WriteLine("Elapsed " + elapsed + " ms, " + throughput + " rows/sec");
                }
            }
            catch (MessageException)
            {
                Console.WriteLine("잘못된 쿼리문입니다");
            }

            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
            client.Close();
        }
    }

    public class ResultCounter : IStreamingResultSet
    {
        public int count = 0;
        public CountdownEvent latch = new CountdownEvent(1);
        /// <summary>
        /// 쿼리 및 쿼리의 결과가 전달 됩니다.
        /// </summary>
        /// <param name="query">쿼리 결과와 연관된 쿼리 개체</param>
        /// <param name="rows">쿼리 결과 행들이 순서에 맞춰 전달됩니다.</param>
        /// <param name="last"> 마지막 onRows() 호출이 수행될 때 true가 전달되고, 그 외에는 false가 전달됩니다.</param>
        public void OnRows(LogQuery query, List<Row> rows, bool last)
        {
            count += rows.Count;
            Console.WriteLine("Received " + count + " rows");

            // 마지막 쿼리 결과를 처리한 후, 대기하는 스레드를 깨워서 종료합니다.
            if (last)
                latch.Signal();
        }
    }
}